package com.dercio.algonated_code_runner.algorithms.scales;

import com.dercio.algonated_code_runner.random.UniformRandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StochasticHillClimbing implements Algorithm<Solution, List<Double>> {

    private static final Logger logger = LoggerFactory.getLogger(StochasticHillClimbing.class);
    private final List<List<Integer>> solutions = new ArrayList<>();
    private double delta = 25;

    @Override
    public Solution run(List<Double> weights, int iterations) {
        logger.info("Running SHC");

        Solution currentSolution = new ScalesSolution(weights.size());
        UniformRandomGenerator randomGenerator = new UniformRandomGenerator();

        for (int i = 0; i < iterations; i++) {
            Solution newSolution = currentSolution.copy();
            newSolution.makeSmallChange();

            double newFitness = newSolution.calculateFitness(weights);
            double currentFitness = currentSolution.calculateFitness(weights);

            double acceptanceProbability = acceptanceProbability(newFitness, currentFitness, delta);

            if (randomGenerator.generateDouble(0, 1) > acceptanceProbability) {
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

    public StochasticHillClimbing setDelta(double delta) {
        this.delta = delta;
        return this;
    }

    private double acceptanceProbability(double newFitness, double oldFitness, double stud) {
        double fitnessDifference = oldFitness - newFitness;
        double fitnessExponent = 1 + Math.exp(fitnessDifference / stud);
        return 1.0 / fitnessExponent;
    }
}
