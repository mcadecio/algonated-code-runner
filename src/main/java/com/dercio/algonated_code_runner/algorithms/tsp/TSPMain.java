package com.dercio.algonated_code_runner.algorithms.tsp;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TSPMain {

    private static final Logger logger = LoggerFactory.getLogger(TSPMain.class);

    public static void main(String[] args) throws IOException {

        String filename = "./fyp-server/CS2004 TSP Data/TSP_48.txt";
        String optFilename = "./fyp-server/CS2004 TSP Data/TSP_48_OPT.txt";

        List<Integer> optimalSolution = Files.readAllLines(Paths.get(optFilename))
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        TSPSolution tspSolution = new TSPSolution(optimalSolution);

        double[][] distances = Files.readAllLines(Paths.get(filename))
                .stream()
                .map(line -> Stream.of(line.split(" "))
                        .mapToDouble(Double::parseDouble)
                        .toArray())
                .toArray(double[][]::new);

        String json = new Gson().toJson(distances);
        logger.info(json);

        final TSPSolution solution = new SimulatedAnnealingAlgorithm()
                .run(distances, 1000000);
        logger.info("{}", solution
                .getSolution());
        logger.info("{}", solution.calculateFitness(distances));
        logger.info("");
        logger.info("{}", tspSolution
                .getSolution());
        logger.info("{}", tspSolution.calculateFitness(distances));

    }

}

