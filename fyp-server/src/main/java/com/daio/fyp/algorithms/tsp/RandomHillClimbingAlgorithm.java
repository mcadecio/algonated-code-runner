package com.daio.fyp.algorithms.tsp;

import com.daio.fyp.algorithms.scales.Algorithm;

import java.util.ArrayList;
import java.util.List;

public class RandomHillClimbingAlgorithm implements Algorithm<TSPSolution, double[][]> {

    private final List<List<Integer>> solutions = new ArrayList<>();

    @Override
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
                solutions.add(currentSolution.getSolution());
                break;
            }

            solutions.add(currentSolution.getSolution());
            System.out.println(currentSolution.calculateFitness(distances));

        }

        return currentSolution;
    }

    @Override
    public List<List<Integer>> getSolutions() {
        return solutions;
    }
}
