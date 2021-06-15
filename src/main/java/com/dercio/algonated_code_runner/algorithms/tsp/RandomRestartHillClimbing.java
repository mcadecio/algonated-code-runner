package com.dercio.algonated_code_runner.algorithms.tsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomRestartHillClimbing extends RandomHillClimbingAlgorithm {

    private static final Logger logger = LoggerFactory.getLogger(RandomRestartHillClimbing.class);
    private int restarts = 10;

    @Override
    public TSPSolution run(double[][] distances, int iterations) {
        logger.info("Running RRHC");

        TSPSolution finalSolution = new TSPSolution(distances.length);

        for (int i = 0; i < restarts; i++) {
            TSPSolution newSolution = super.run(distances, iterations / restarts);

            if (newSolution.calculateFitness(distances) < finalSolution.calculateFitness(distances)) {
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
