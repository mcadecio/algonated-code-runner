package com.daio.fyp.algorithms.tsp;

import com.daio.fyp.random.UniformRandomGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TSPSolution {
    private static final UniformRandomGenerator randomGenerator = new UniformRandomGenerator();
    private final List<Integer> solution;

    public TSPSolution(int length) {
        this(generateRandomList(length));
    }

    public TSPSolution(List<Integer> solution) {
        this.solution = solution;
    }


    public double calculateFitness(double[][] distances) {
        if (solution.size() != distances.length) return (-1);

        int numberOfCities = solution.size();

        double sum = 0;

        for (int i = 0; i < numberOfCities - 1; i++) {
            int city = solution.get(i);
            int nextCity = solution.get(i + 1);
            sum = sum + distances[city][nextCity];
        }

        int endCity = solution.get(solution.size() - 1);
        int startCity = solution.get(0);

        sum = sum + distances[endCity][startCity];

        return sum;
    }

    public void makeSmallChange() {
        int firstCity = 0;
        int secondCity = 0;

        while (firstCity == secondCity) {
            firstCity = randomGenerator.generateInteger(0, solution.size() - 1);
            secondCity = randomGenerator.generateInteger(0, solution.size() - 1);
        }

        Collections.swap(solution, firstCity, secondCity);
    }

    public List<Integer> getSolution() {
        return solution;
    }

    public TSPSolution copy() {
        return new TSPSolution(new ArrayList<>(solution));
    }

    private static List<Integer> generateRandomList(int size) {
        List<Integer> orderedList = createZeroList(size);
        List<Integer> shuffledList = new ArrayList<>();

        while (!orderedList.isEmpty()) {
            int randomIndex = randomGenerator.generateInteger(0, orderedList.size() - 1);
            shuffledList.add(orderedList.get(randomIndex));
            orderedList.remove(randomIndex);
        }

        return shuffledList;
    }

    private static List<Integer> createZeroList(int size) {
        List<Integer> zeroList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            zeroList.add(i);
        }

        return zeroList;
    }
}
