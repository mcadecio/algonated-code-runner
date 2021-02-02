package com.daio.fyp.runner.demo;

import com.daio.fyp.Timer;
import com.daio.fyp.algorithms.scales.Algorithm;
import com.daio.fyp.algorithms.tsp.RandomHillClimbingAlgorithm;
import com.daio.fyp.algorithms.tsp.SimulatedAnnealingAlgorithm;
import com.daio.fyp.algorithms.tsp.TSPSolution;
import com.daio.fyp.response.Response;
import com.daio.fyp.runner.CodeRunnerSummary;
import com.daio.fyp.runner.calculator.TSPEfficiencyCalculator;
import com.daio.fyp.runner.calculator.TSPFitnessCalculator;
import com.google.common.base.Stopwatch;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TSPRunner {

    private final DemoRequest<double[][]> request;
    private final CodeRunnerSummary summary = new CodeRunnerSummary();
    private List<Integer> solution = Collections.emptyList();
    private List<List<Integer>> solutions = Collections.emptyList();

    public TSPRunner(DemoRequest<double[][]> request) {
        this.request = request;
    }

    public TSPRunner run() {
        Algorithm<TSPSolution, double[][]> algorithm = new SimulatedAnnealingAlgorithm();
        if (request.getAlgorithm().equals("simulatedannealing")) {
            algorithm = new SimulatedAnnealingAlgorithm();
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
        Timer.runTimedTask(() -> {
            summary.setFitness(new TSPFitnessCalculator().calculate(request.getData(), solution));
        }, "Fitness Calculator");

        Timer.runTimedTask(() -> {
            summary.setEfficacy(new TSPEfficiencyCalculator().calculate(request.getData(), solution));
        }, "Efficacy Calculator");
    }

    public CodeRunnerSummary getSummary() {
        return summary;
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
