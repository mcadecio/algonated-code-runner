package com.daio.fyp.runner.calculator;

import com.daio.fyp.algorithms.scales.ScalesSolution;
import com.daio.fyp.algorithms.scales.Solution;

import java.util.List;

public class ScalesFitnessCalculator implements Calculator<List<Double>> {

    @Override
    public double calculate(List<Double> data, List<Integer> solution) {
        Solution scalesSolution = new ScalesSolution(solution);

        return scalesSolution.calculateFitness(data);
    }
}
