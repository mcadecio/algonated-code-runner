package com.daio.fyp.algorithms.tsp;

import com.daio.fyp.algorithms.scales.Algorithm;
import com.daio.fyp.random.UniformRandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StochasticHillClimbing implements Algorithm<TSPSolution, double[][]> {

    private static final Logger logger = LoggerFactory.getLogger(StochasticHillClimbing.class);
    private final List<List<Integer>> solutions = new ArrayList<>();
    private double delta = 25;

    @Override
    public TSPSolution run(double[][] distances, int iterations) {
        logger.info("Running SHC");

        TSPSolution currentSolution = new TSPSolution(distances.length);
        UniformRandomGenerator randomGenerator = new UniformRandomGenerator();
        for (int i = 0; i < iterations; i++) {
            TSPSolution newSolution = currentSolution.copy();
            newSolution.makeSmallChange();

            double newFitness = newSolution.calculateFitness(distances);
            double currentFitness = currentSolution.calculateFitness(distances);

            double acceptanceProbability = acceptanceProbability(newFitness, currentFitness, delta);

            if (randomGenerator.generateInteger(0, 1) > acceptanceProbability) {
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
        double fitnessDifference = newFitness - oldFitness;
        double fitnessExponent = 1 + Math.exp(fitnessDifference / stud);
        return 1.0 / fitnessExponent;
    }
}
