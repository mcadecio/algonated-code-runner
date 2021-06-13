package com.daio.fyp.runner;

import com.daio.fyp.response.Response;
import com.daio.fyp.runner.calculator.ScalesEfficiencyCalculator;
import com.daio.fyp.runner.calculator.ScalesFitnessCalculator;
import com.daio.fyp.runner.executor.SelfClosingExecutor;
import com.daio.fyp.verifier.IllegalMethodVerifier;
import com.daio.fyp.verifier.Verifier;
import com.google.common.base.Stopwatch;
import org.joor.Reflect;
import org.joor.ReflectException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScalesCodeRunner {

    private static final String PLEASE_REMOVE = "Please remove the following ";

    private String errorMessage;
    private boolean isSuccess;
    private Reflect compiledClass;
    private final CodeOptions options;
    private final CodeRunnerSummary runnerSummary;
    private List<List<Integer>> solutions = Collections.emptyList();
    private List<Integer> solution = Collections.emptyList();
    private final List<Double> weights;

    public ScalesCodeRunner(CodeOptions options, List<Double> weights) {
        this.options = options;
        this.isSuccess = false;
        this.errorMessage = "It seems compile was never called!";
        this.runnerSummary = new CodeRunnerSummary();
        this.weights = weights;
    }

    public ScalesCodeRunner compile() {

        if (verifyError(new IllegalMethodVerifier(options.getIllegalMethods()), "illegal methods:\n")) {
            return this;
        }

        try {
            compiledClass = compileClass();
            isSuccess = true;
            errorMessage = "";
        } catch (ReflectException reflectException) {
            handleError(reflectException.getMessage());
            return this;
        }

        return this;
    }

    public ScalesCodeRunner execute() {

        if (!isSuccess) {
            return this;
        }

        try {
            Stopwatch timer = Stopwatch.createStarted();
            String rawSolution = compiledClass.call(options.getMethodToCall(), weights, options.getIterations())
                    .get();
            timer.stop();


            List<String> stringSolutions = compiledClass.get("solutions");

            solutions = binaryStringToList(stringSolutions);

            solution = transformStringToList(rawSolution);
            isSuccess = true;
            errorMessage = "Compile and Run was a success!";
            buildSummary(timer.elapsed(TimeUnit.MILLISECONDS));
        } catch (ReflectException | ExecutionException exception) {
            handleError(exception.getMessage());
        } catch (InterruptedException interruptedException) {
            handleError(interruptedException.getMessage());
            Thread.currentThread().interrupt();
        }
        return this;
    }

    public void buildSummary(long elapsed) {
        runnerSummary.setTimeRun(elapsed)
                .setIterations(options.getIterations())
                .setFitness(new ScalesFitnessCalculator().calculate(weights, solution))
                .setEfficacy(new ScalesEfficiencyCalculator().calculate(weights, solution));
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public List<List<Integer>> getSolutions() {
        return solutions;
    }

    private void handleError(String errorMessage) {
        this.errorMessage = errorMessage;
        isSuccess = false;
    }

    private boolean verifyError(Verifier verifier, String errorMessage) {
        final List<String> errors = verifier.verify(options.getCode());
        if (!errors.isEmpty()) {
            handleError(PLEASE_REMOVE + errorMessage + String.join("\n", errors));
            return true;
        }
        return false;
    }

    private Reflect compileClass() {
        String packageName = "package " + options.getPackageName() + ";";
        String className = options.getPackageName() + "." + options.getClassName();
        return Reflect.compile(className, packageName +
                "\n" +
                String.join("\n", options.getImportsAllowed()) +
                "\n" +
                options.getCode()
        ).create();
    }

    private List<Integer> transformStringToList(String solution) {
        return Stream.of(solution.split(""))
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private List<List<Integer>> binaryStringToList(List<String> rawSolutions) throws InterruptedException, ExecutionException {
        if (rawSolutions.size() < 100) {
            return rawSolutions.stream()
                    .map(this::transformStringToList)
                    .collect(Collectors.toList());
        }

        double nPartitions = 10.0;

        List<List<String>> batches = createBatches(nPartitions, rawSolutions);

        return runBatches(batches, nPartitions);
    }

    public List<Integer> getSolution() {
        return solution;
    }

    public Response toResponse() {
        return new Response()
                .setSuccess(isSuccess())
                .setConsoleOutput(getErrorMessage())
                .setResult(getSolution())
                .setData(weights)
                .setSummary(runnerSummary)
                .setSolutions(getSolutions());
    }

    private List<String> getListFromTo(List<String> list, int counter, int size) {
        List<String> strings = new ArrayList<>();
        for (int i = counter; i < counter + size; i++) {
            strings.add(list.get(i));
        }
        return strings;
    }


    private Callable<List<List<Integer>>> toCallableTask(List<String> batch) {
        return () -> batch.stream()
                .map(this::transformStringToList)
                .collect(Collectors.toList());
    }

    private List<List<String>> createBatches(double nPartitions, List<String> rawSolutions) {
        int batchNumber = (int) Math.ceil(rawSolutions.size() / nPartitions);
        int counter = 0;

        List<List<String>> batches = new ArrayList<>();
        for (int i = 0; i < nPartitions - 1; i++) {
            batches.add(getListFromTo(rawSolutions, counter, batchNumber));
            counter = counter + batchNumber;
        }
        batches.add(getListFromTo(rawSolutions, counter, rawSolutions.size() - counter));

        return batches;
    }

    private List<List<Integer>> runBatches(List<List<String>> batches, double nPartitions) throws InterruptedException, ExecutionException {
        List<List<Integer>> result = new ArrayList<>();
        try (SelfClosingExecutor selfClosingExecutor = new SelfClosingExecutor((int) nPartitions)) {

            List<Future<List<List<Integer>>>> futures = selfClosingExecutor
                    .invokeAll(batches.stream()
                            .map(this::toCallableTask)
                            .collect(Collectors.toList()));

            for (Future<List<List<Integer>>> future : futures) {
                result.addAll(future.get());
            }
        }
        return result;
    }
}

