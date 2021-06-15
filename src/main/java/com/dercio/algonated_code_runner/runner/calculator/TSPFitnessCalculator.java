package com.dercio.algonated_code_runner.runner.calculator;

import com.dercio.algonated_code_runner.algorithms.tsp.TSPSolution;

import java.util.List;

public class TSPFitnessCalculator implements Calculator<double[][]> {

    @Override
    public double calculate(double[][] data, List<Integer> solution) {
        return new TSPSolution(solution).calculateFitness(data);
    }
}
