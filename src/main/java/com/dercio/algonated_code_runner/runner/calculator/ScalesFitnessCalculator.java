package com.dercio.algonated_code_runner.runner.calculator;

import com.dercio.algonated_code_runner.algorithms.scales.ScalesSolution;
import com.dercio.algonated_code_runner.algorithms.scales.Solution;

import java.util.List;

public class ScalesFitnessCalculator implements Calculator<List<Double>> {

    @Override
    public double calculate(List<Double> data, List<Integer> solution) {
        Solution scalesSolution = new ScalesSolution(solution);

        return scalesSolution.calculateFitness(data);
    }
}
