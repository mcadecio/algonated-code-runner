package com.daio.fyp.algorithms.scales;


import com.daio.fyp.random.UniformRandomGenerator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScalesSolution implements Solution {
    private static final UniformRandomGenerator randomGenerator = new UniformRandomGenerator();
    private String solution;

    public ScalesSolution(int length) {
        this(generateRandomBinaryString(length));
    }

    public ScalesSolution(String solution) {
        this.solution = solution;
    }

    @Override
    public double calculateFitness(List<Double> weights) {
        if (solution.length() > weights.size()) return (-1);
        double leftHandSide = 0.0;
        double rightHandSide = 0.0;
        int n = solution.length();

        for (int i = 0; i < n; i++) {
            if (solution.charAt(i) == '0') {
                leftHandSide += weights.get(i);
            } else
                rightHandSide += weights.get(i);
        }

        return (Math.abs(leftHandSide - rightHandSide));
    }

    @Override
    public void makeSmallChange() {
        int length = solution.length();

        int randomInt = randomGenerator.generateInteger(0, length - 1);
        char targetChar = solution.charAt(randomInt);
        StringBuilder strBuilder = new StringBuilder(solution);

        if (targetChar == '1') {
            strBuilder.replace(randomInt, randomInt + 1, "0");
        } else {
            strBuilder.replace(randomInt, randomInt + 1, "1");
        }

        solution = strBuilder.toString();
    }

    @Override
    public List<Integer> getSolution() {
        return Stream.of(solution.split(""))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    @Override
    public ScalesSolution copy() {
        return new ScalesSolution(this.solution);
    }

    private static String generateRandomBinaryString(int n) {
        String s = "";
        for (int i = 0; i < n; i++) {
            String randomChar = String.valueOf(randomGenerator.generateInteger(0, 1));
            if (randomChar.equals("0")) {
                s = s.concat(randomChar);
            } else {
                s = randomChar.concat(s);
            }
        }

        return (s);
    }
}

