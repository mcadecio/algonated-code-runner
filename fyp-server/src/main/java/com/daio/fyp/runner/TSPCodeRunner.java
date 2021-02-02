package com.daio.fyp.runner;

import com.daio.fyp.Timer;
import com.daio.fyp.response.Response;
import com.daio.fyp.runner.calculator.TSPEfficiencyCalculator;
import com.daio.fyp.runner.calculator.TSPFitnessCalculator;
import com.daio.fyp.verifier.IllegalMethodVerifier;
import com.daio.fyp.verifier.Verifier;
import com.google.common.base.Stopwatch;
import org.joor.Reflect;
import org.joor.ReflectException;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TSPCodeRunner {

    private static final String PLEASE_REMOVE = "Please remove the following ";

    private String errorMessage;
    private boolean isSuccess;
    private Reflect compiledClass;
    private final CodeOptions options;
    private final CodeRunnerSummary runnerSummary;
    private List<List<Integer>> solutions;
    private List<Integer> solution;
    private List<List<Double>> distances;

    public TSPCodeRunner(CodeOptions options, List<List<Double>> distances)  {
        this.options = options;
        this.distances = distances;
        this.isSuccess = false;
        this.errorMessage = "It seems compile was never called!";
        this.runnerSummary = new CodeRunnerSummary();
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
            runnerSummary.setTimeRun(timer.elapsed(TimeUnit.MILLISECONDS));
            runnerSummary.setIterations(options.getIterations());

            solutions =  compiledClass.get("solutions");
            isSuccess = true;
            errorMessage = "Compile and Run was a success!";
            return this;
        } catch (ReflectException reflectException) {
            handleError(reflectException.getMessage());
            return this;
        }
    }

    public CodeRunnerSummary getSummary() {
        Timer.runTimedTask(() -> {
            runnerSummary.setFitness(new TSPFitnessCalculator().calculate(distancesToArray(), solution));
        }, "Fitness Calculator");

        Timer.runTimedTask(() -> {
            runnerSummary.setEfficacy(new TSPEfficiencyCalculator().calculate(distancesToArray(), solution));
        }, "Efficacy Calculator");

        return runnerSummary;
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
                .setSummary(getSummary())
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

}

