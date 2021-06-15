package com.dercio.algonated_code_runner.runner.calculator;

import com.dercio.algonated_code_runner.algorithms.scales.SimulatedAnnealingAlgorithm;

import java.util.List;

public class ScalesEfficiencyCalculator implements Calculator<List<Double>> {

    @Override
    public double calculate(List<Double> data, List<Integer> candidateSolution) {
        double candidateFitness = new ScalesFitnessCalculator().calculate(data, candidateSolution);
        double optimalFitness = new SimulatedAnnealingAlgorithm()
                .run(data, 1000)
                .calculateFitness(data);

        return (optimalFitness - candidateFitness) * 100;
    }
}
