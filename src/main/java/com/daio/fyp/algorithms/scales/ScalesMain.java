package com.daio.fyp.algorithms.scales;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ScalesMain {
    private static final Logger logger = LoggerFactory.getLogger(ScalesMain.class);

    public static void main(String[] args) throws IOException {
        String filename = "/Users/ddaio/Downloads/Primes.txt";

        List<Double> weights = Files.readAllLines(Paths.get(filename))
                .stream()
                .map(Double::parseDouble)
                .collect(Collectors.toList());

        final Solution solution = new RandomHillClimbingAlgorithm()
                .run(weights, 10000);

        logger.info("{}", solution.getSolution());
        logger.info("{}", solution.calculateFitness(weights));
    }
}
