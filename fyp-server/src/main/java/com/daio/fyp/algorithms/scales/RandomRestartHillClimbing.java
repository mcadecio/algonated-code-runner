package com.daio.fyp.algorithms.scales;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RandomRestartHillClimbing extends RandomHillClimbingAlgorithm {

    private static final Logger logger = LoggerFactory.getLogger(RandomRestartHillClimbing.class);
    private int restarts = 10;

    @Override
    public Solution run(List<Double> weights, int iterations) {
        logger.info("Running RRHC");

        Solution finalSolution = new ScalesSolution(weights.size());

        for (int i = 0; i < restarts; i++) {
            Solution newSolution = super.run(weights, iterations / restarts);

            if (newSolution.calculateFitness(weights) < finalSolution.calculateFitness(weights)) {
                finalSolution = newSolution.copy();
            }
        }

        return finalSolution;
    }

    public RandomHillClimbingAlgorithm setRestarts(int restarts) {
        this.restarts = restarts;
        return this;
    }

}
