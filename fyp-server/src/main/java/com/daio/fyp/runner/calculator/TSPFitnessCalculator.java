package com.daio.fyp.runner.calculator;

import com.daio.fyp.algorithms.tsp.TSPSolution;

import java.util.List;

public class TSPFitnessCalculator implements Calculator<double[][]> {

    @Override
    public double calculate(double[][] data, List<Integer> solution) {
        return new TSPSolution(solution).calculateFitness(data);
    }
}
