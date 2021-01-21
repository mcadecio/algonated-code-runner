package com.daio.fyp.algorithms.tsp;

public class RandomHillClimbingAlgorithm {

    public TSPSolution run(double[][] distances, int iterations) {
        System.out.println("Running RMHC");

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
                break;
            }

            System.out.println(currentSolution.calculateFitness(distances));

        }

        return currentSolution;
    }

}
