package com.daio.fyp.algorithms.scales;

import com.daio.fyp.algorithms.tsp.TSPSolution;
import com.daio.fyp.runner.demo.DemoRequest;

import java.util.List;

public interface Algorithm<T, D> {
    T run(D weights, int iterations);

    List<List<Integer>> getSolutions();

    static Algorithm<Solution, List<Double>> getScalesAlgorithm(DemoRequest<List<Double>> request) {
        Algorithm<Solution, List<Double>> algorithm;
        String requestedAlgorithm = request.getAlgorithm();
        if (requestedAlgorithm.equals("sa")) {
            algorithm = new SimulatedAnnealingAlgorithm()
                    .setOptionalCR(request.getCoolingRate())
                    .setOptionalTemp(request.getTemperature());
        } else if (requestedAlgorithm.equals("rrhc")) {
            algorithm = new RandomRestartHillClimbing()
                    .setRestarts(request.getRestarts());
        } else if (requestedAlgorithm.equals("shc")) {
            algorithm = new StochasticHillClimbing()
                    .setDelta(request.getDelta());
        } else {
            algorithm = new RandomHillClimbingAlgorithm();
        }

        return algorithm;
    }

    static Algorithm<TSPSolution, double[][]> getTSPAlgorithm(DemoRequest<double[][]> request) {
        Algorithm<TSPSolution, double[][]> algorithm;
        String requestedAlgorithm = request.getAlgorithm();
        if (requestedAlgorithm.equals("sa")) {
            algorithm = new com.daio.fyp.algorithms.tsp.SimulatedAnnealingAlgorithm()
                    .setOptionalCR(request.getCoolingRate())
                    .setOptionalTemp(request.getTemperature());
        } else if (requestedAlgorithm.equals("rrhc")) {
            algorithm = new com.daio.fyp.algorithms.tsp.RandomRestartHillClimbing()
                    .setRestarts(request.getRestarts());
        } else if (requestedAlgorithm.equals("shc")) {
            algorithm = new com.daio.fyp.algorithms.tsp.StochasticHillClimbing()
                    .setDelta(request.getDelta());
        } else {
            algorithm = new com.daio.fyp.algorithms.tsp.RandomHillClimbingAlgorithm();
        }

        return algorithm;
    }
}

