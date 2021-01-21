package com.daio.fyp.runner.calculator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TSPEfficiencyCalculator implements Calculator<double[][]> {

    @Override
    public double calculate(double[][] data, List<Integer> solution) {
        String optFilename = "./TSP_DATA/TSP_" + data.length + "_OPT.txt";

        List<Integer> rawOptimalSolution;
        try (Stream<String> lines = Files.lines(Paths.get(optFilename))) {
            rawOptimalSolution = lines
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        double candidateFitness = new TSPFitnessCalculator().calculate(data, solution);
        double optimalFitness = new TSPFitnessCalculator().calculate(data, rawOptimalSolution);

        return (optimalFitness / candidateFitness) * 100;
    }
}
