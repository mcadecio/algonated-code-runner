package com.daio.fyp.runner.demo;

import com.daio.fyp.algorithms.scales.Algorithm;
import com.daio.fyp.algorithms.scales.RandomHillClimbingAlgorithm;
import com.daio.fyp.algorithms.scales.SimulatedAnnealingAlgorithm;
import com.daio.fyp.algorithms.scales.Solution;
import com.daio.fyp.response.Response;
import com.daio.fyp.runner.CodeRunnerSummary;
import com.daio.fyp.runner.calculator.ScalesEfficiencyCalculator;
import com.daio.fyp.runner.calculator.ScalesFitnessCalculator;
import com.google.common.base.Stopwatch;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScalesRunner {

    private final DemoRequest<List<Double>> request;
    private final CodeRunnerSummary summary = new CodeRunnerSummary();
    private List<Integer> solution = Collections.emptyList();
    private List<List<Integer>> solutions = Collections.emptyList();

    public ScalesRunner(DemoRequest<List<Double>> request) {
        this.request = request;
    }

    public ScalesRunner run() {
        Algorithm<Solution, List<Double>> algorithm = new SimulatedAnnealingAlgorithm();
        if (request.getAlgorithm().equals("simulatedannealing")) {
            algorithm = new SimulatedAnnealingAlgorithm()
                    .setOptionalCR(request.getCoolingRate())
                    .setOptionalTemp(request.getTemperature());
        } else if (request.getAlgorithm().equals("randomhillclimbing")) {
            algorithm = new RandomHillClimbingAlgorithm();
        }

        Stopwatch timer = Stopwatch.createStarted();
        solution = algorithm.run(request.getData(), request.getIterations()).getSolution();
        timer.stop();

        solutions = algorithm.getSolutions();

        buildSummary(timer.elapsed(TimeUnit.MILLISECONDS));
        return this;
    }

    private void buildSummary(long elapsed) {
        summary.setTimeRun(elapsed);
        summary.setIterations(request.getIterations());
        summary.setFitness(new ScalesFitnessCalculator().calculate(request.getData(), solution));
        summary.setEfficacy(new ScalesEfficiencyCalculator().calculate(request.getData(), solution));
    }

    public Response toResponse() {
        return new Response()
                .setSuccess(true)
                .setConsoleOutput("Compile and Run was a success!")
                .setResult(solution)
                .setData(request.getData())
                .setSummary(summary)
                .setSolutions(solutions);
    }
}
