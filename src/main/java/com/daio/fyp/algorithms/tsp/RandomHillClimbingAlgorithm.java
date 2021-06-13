package com.daio.fyp.algorithms.tsp;

import com.daio.fyp.algorithms.scales.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RandomHillClimbingAlgorithm implements Algorithm<TSPSolution, double[][]> {

    private static final Logger logger = LoggerFactory.getLogger(RandomHillClimbingAlgorithm.class);
    private final List<List<Integer>> solutions = new ArrayList<>();

    @Override
    public TSPSolution run(double[][] distances, int iterations) {
        logger.info("Running RMHC");

        TSPSolution currentSolution = new TSPSolution(distances.length);

        for (int i = 0; i < iterations; i++) {
            TSPSolution newSolution = currentSolution.copy();
            newSolution.makeSmallChange();

            double newFitness = newSolution.calculateFitness(distances);
            double currentFitness = currentSolution.calculateFitness(distances);

            if (newFitness < currentFitness) {
                currentSolution = newSolution.copy();
            }

            if (newFitness == 0) {
                currentSolution = newSolution.copy();
                solutions.add(currentSolution.getSolution());
                break;
            }

            solutions.add(currentSolution.getSolution());
        }

        return currentSolution;
    }

    @Override
    public List<List<Integer>> getSolutions() {
        return solutions;
    }
}
