package com.daio.fyp.runner;

import com.daio.fyp.response.Response;
import com.daio.fyp.runner.calculator.TSPEfficiencyCalculator;
import com.daio.fyp.runner.calculator.TSPFitnessCalculator;
import com.daio.fyp.verifier.IllegalMethodVerifier;
import com.daio.fyp.verifier.Verifier;
import com.google.common.base.Stopwatch;
import org.joor.Reflect;
import org.joor.ReflectException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TSPCodeRunner {

    private static final String PLEASE_REMOVE = "Please remove the following ";

    private final CodeRunnerSummary runnerSummary = new CodeRunnerSummary();
    private boolean isSuccess;
    private Reflect compiledClass;
    private final CodeOptions options;
    private String errorMessage = "It seems compile was never called!";
    private List<List<Integer>> solutions = Collections.emptyList();
    private List<Integer> solution = Collections.emptyList();
    private final List<List<Double>> distances;

    public TSPCodeRunner(CodeOptions options, List<List<Double>> distances) {
        this.options = options;
        this.distances = distances;
    }

    public TSPCodeRunner compile() {

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

    public TSPCodeRunner execute() {

        if (!isSuccess) {
            return this;
        }

        double[][] data = distancesToArray();

        try {
            Stopwatch timer = Stopwatch.createStarted();
            solution = compiledClass.call(options.getMethodToCall(), data, options.getIterations())
                    .get();
            timer.stop();

            buildSummary(timer.elapsed(TimeUnit.MILLISECONDS));

            solutions = compiledClass.get("solutions");
            isSuccess = true;
            errorMessage = "Compile and Run was a success!";
            return this;
        } catch (ReflectException reflectException) {
            handleError(reflectException.getMessage());
            return this;
        }
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


    public List<Integer> getSolution() {
        return solution;
    }

    public Response toResponse() {
        return new Response()
                .setSuccess(isSuccess())
                .setConsoleOutput(getErrorMessage())
                .setResult(getSolution())
                .setData(distances)
                .setSummary(runnerSummary)
                .setSolutions(getSolutions());
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

    private double[][] distancesToArray() {
        return distances.stream().map(listOfList -> listOfList.stream()
                .mapToDouble(Double::doubleValue)
                .toArray()).toArray(double[][]::new);
    }

    private void buildSummary(long elapsed) {
        runnerSummary.setTimeRun(elapsed)
                .setIterations(options.getIterations())
                .setFitness(new TSPFitnessCalculator().calculate(distancesToArray(), solution))
                .setEfficacy(new TSPEfficiencyCalculator().calculate(distancesToArray(), solution));
    }

}

