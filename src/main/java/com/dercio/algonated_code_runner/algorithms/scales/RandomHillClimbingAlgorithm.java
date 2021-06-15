package com.dercio.algonated_code_runner.algorithms.scales;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RandomHillClimbingAlgorithm implements Algorithm<Solution, List<Double>> {

    private static final Logger logger = LoggerFactory.getLogger(RandomHillClimbingAlgorithm.class);
    private final List<List<Integer>> solutions = new ArrayList<>();

    @Override
    public Solution run(List<Double> weights, int iterations) {
        logger.info("Running RMHC");

        Solution currentSolution = new ScalesSolution(weights.size());
        solutions.add(currentSolution.getSolution());

        for (int i = 0; i < iterations; i++) {
            Solution newSolution = currentSolution.copy();
            newSolution.makeSmallChange();

            double newFitness = newSolution.calculateFitness(weights);
            double currentFitness = currentSolution.calculateFitness(weights);

            if (newFitness < currentFitness) {
                currentSolution = newSolution.copy();
            }
            solutions.add(currentSolution.getSolution());

            if (newFitness == 0) {
                currentSolution = newSolution.copy();
                break;
            }

        }

        return currentSolution;
    }

    @Override
    public List<List<Integer>> getSolutions() {
        return solutions;
    }
}
