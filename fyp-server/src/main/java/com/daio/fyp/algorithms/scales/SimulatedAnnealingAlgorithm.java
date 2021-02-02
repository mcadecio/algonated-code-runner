package com.daio.fyp.algorithms.scales;

import com.daio.fyp.random.UniformRandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class SimulatedAnnealingAlgorithm implements Algorithm<Solution, List<Double>> {

    private final UniformRandomGenerator randomGenerator = new UniformRandomGenerator();
    private final List<List<Integer>> solutions = new ArrayList<>();

    @Override
    public Solution run(List<Double> weights, int iterations) {
        System.out.println("Running SA");

        double temperature = 1000;
        double coolingRate = calcCR(temperature, iterations);

        Solution finalSolution = new ScalesSolution(weights.size());

        for (int i = 0; i < iterations; i++) {
            finalSolution = calculateNewSolution(weights, temperature, finalSolution);
            temperature = coolingRate * temperature;
            solutions.add(finalSolution.getSolution());
        }

        return finalSolution;
    }

    @Override
    public List<List<Integer>> getSolutions() {
        return solutions;
    }

    private Solution calculateNewSolution(List<Double> weights, double temperature, Solution finalSolution) {
        Solution temporarySolution = finalSolution.copy();
        temporarySolution.makeSmallChange();
        double temporarySolutionFitness = temporarySolution.calculateFitness(weights);

        double finalSolutionFitness = finalSolution.calculateFitness(weights);

        if (temporarySolutionFitness > finalSolutionFitness) {
            double changeProbability = acceptanceFunction(temporarySolutionFitness, finalSolutionFitness, temperature);

            if (changeProbability > randomGenerator.generateDouble(0, 1)) {
                finalSolution = temporarySolution.copy();
            }

        } else {
            finalSolution = temporarySolution.copy();
        }

        return finalSolution;
    }

    private double acceptanceFunction(double newFitness, double oldFitness, double temperature) {
        double delta = Math.abs(oldFitness - newFitness);
        delta = -1 * delta;
        return Math.exp(delta / temperature);
    }

    private double calcCR(double temperature, int nIterations) {
        double tIter = 0.001;
        double power = 1.0 / nIterations;
        double tValue = tIter / temperature;

        return Math.pow(tValue, power);
    }

}
