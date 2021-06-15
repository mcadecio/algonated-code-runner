package com.dercio.algonated_code_runner.algorithms.scales;

import com.dercio.algonated_code_runner.random.UniformRandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimulatedAnnealingAlgorithm implements Algorithm<Solution, List<Double>> {

    private static final Logger logger = LoggerFactory.getLogger(SimulatedAnnealingAlgorithm.class);
    private final UniformRandomGenerator randomGenerator = new UniformRandomGenerator();
    private final List<List<Integer>> solutions = new ArrayList<>();
    private Optional<Double> optionalTemp = Optional.empty();
    private Optional<Double> optionalCR = Optional.empty();

    @Override
    public Solution run(List<Double> weights, int iterations) {
        logger.info("Running SA");

        double temperature = optionalTemp.orElse(1000.0);
        double coolingRate = optionalCR.orElse(calcCR(temperature, iterations));

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


    public SimulatedAnnealingAlgorithm setOptionalTemp(double optionalTemp) {
        this.optionalTemp = Optional.of(optionalTemp);
        return this;
    }

    public SimulatedAnnealingAlgorithm setOptionalCR(double optionalCR) {
        this.optionalCR = Optional.of(optionalCR);
        return this;
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
