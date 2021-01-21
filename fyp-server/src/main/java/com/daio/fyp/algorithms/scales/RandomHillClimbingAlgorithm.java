package com.daio.fyp.algorithms.scales;

import java.util.List;

public class RandomHillClimbingAlgorithm implements Algorithm<Solution> {

    @Override
    public Solution run(List<Double> weights, int iterations) {
        System.out.println("Running RMHC");

        Solution currentSolution = new ScalesSolution(weights.size());

        for (int i = 0; i < iterations; i++) {
            Solution newSolution = currentSolution.copy();
            newSolution.makeSmallChange();

            double newFitness = newSolution.calculateFitness(weights);
            double currentFitness = currentSolution.calculateFitness(weights);

            if (newFitness < currentFitness) {
                currentSolution = newSolution.copy();
            }

            if (newFitness == 0) {
                currentSolution = newSolution.copy();
                break;
            }

        }

        return currentSolution;
    }

}
