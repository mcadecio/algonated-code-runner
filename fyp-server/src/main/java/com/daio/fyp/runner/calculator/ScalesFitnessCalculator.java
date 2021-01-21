package com.daio.fyp.runner.calculator;

import com.daio.fyp.algorithms.scales.ScalesSolution;
import com.daio.fyp.algorithms.scales.Solution;

import java.util.List;
import java.util.stream.Collectors;

public class ScalesFitnessCalculator implements Calculator<List<Double>> {

    @Override
    public double calculate(List<Double> data, List<Integer> solution) {
        Solution scalesSolution = new ScalesSolution(solution.stream()
                .map(String::valueOf)
                .collect(Collectors.joining()));

        return scalesSolution.calculateFitness(data);
    }
}
