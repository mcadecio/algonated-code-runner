package com.daio.fyp.algorithms.tsp;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TSPMain {

    public static void main(String[] args) throws IOException {

        String filename = "./fyp-server/CS2004 TSP Data/TSP_48.txt";
        String optFilename = "./fyp-server/CS2004 TSP Data/TSP_48_OPT.txt";

        List<Integer> optimalSolution = Files.lines(Paths.get(optFilename))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        TSPSolution tspSolution = new TSPSolution(optimalSolution);

        double[][] distances = Files.lines(Paths.get(filename))
                .map(line -> Stream.of(line.split(" "))
                        .mapToDouble(Double::parseDouble)
                        .toArray())
                .toArray(double[][]::new);

        System.out.println(new Gson().toJson(distances));

        final TSPSolution solution = new SimulatedAnnealingAlgorithm()
                .run(distances, 1000000);
        System.out.println(solution
                .getSolution());
        System.out.println(solution.calculateFitness(distances));
        System.out.println();
        System.out.println(tspSolution
                .getSolution());
        System.out.println(tspSolution.calculateFitness(distances));

    }

}

