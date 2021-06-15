package com.dercio.algonated_code_runner.algorithms.scales;


import com.dercio.algonated_code_runner.random.UniformRandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class ScalesSolution implements Solution {
    private static final UniformRandomGenerator randomGenerator = new UniformRandomGenerator();
    private final List<Integer> solution;

    public ScalesSolution(int length) {
        this(generateRandomBinaryString(length));
    }

    public ScalesSolution(List<Integer> solution) {
        this.solution = solution;
    }

    private static List<Integer> generateRandomBinaryString(int n) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int randomChar = randomGenerator.generateInteger(0, 1);
            if (randomChar == 0) {
                integers.add(0, 0);
            } else {
                integers.add(i, 1);
            }
        }

        return integers;
    }

    @Override
    public double calculateFitness(List<Double> weights) {
        if (solution.size() > weights.size()) return (-1);
        double leftHandSide = 0.0;
        double rightHandSide = 0.0;
        int n = solution.size();

        for (int i = 0; i < n; i++) {
            if (solution.get(i) == 0) {
                leftHandSide += weights.get(i);
            } else
                rightHandSide += weights.get(i);
        }

        return (Math.abs(leftHandSide - rightHandSide));
    }

    @Override
    public void makeSmallChange() {
        int length = solution.size();

        int randomInt = randomGenerator.generateInteger(0, length - 1);
        int targetChar = solution.get(randomInt);

        solution.remove(targetChar);
        if (targetChar == 1) {
            solution.add(randomInt, 0);
        } else {
            solution.add(randomInt, 1);
        }

    }

    @Override
    public List<Integer> getSolution() {
        return solution;
    }

    @Override
    public ScalesSolution copy() {
        return new ScalesSolution(new ArrayList<>(this.solution));
    }
}

