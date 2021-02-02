package newer.tsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TSPProblem {
    public List<Integer> runTSP(double[][] distances, int iterations) {
        return new SimulatedAnnealingAlgorithm()
                .run(distances, iterations)
                .getSolution();
    }
}


class TSPSolution {
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

class UniformRandomGenerator {
    private final Random random;

    public UniformRandomGenerator() {
        random = new Random();
        random.setSeed(System.nanoTime());
    }

    public int generateInteger(int lower, int upper) {
        int bound = upper - lower + 1;
        return (random.nextInt(bound) + lower);
    }

    public double generateDouble(double lower, double upper) {
        return ((upper - lower) * random.nextDouble() + lower);
    }
}


class SimulatedAnnealingAlgorithm {

    private final UniformRandomGenerator randomGenerator = new UniformRandomGenerator();

    public TSPSolution run(double[][] distances, int iterations) {
        System.out.println("Running SA");

        double temperature = calculateTemp(distances);
        double coolingRate = calcCR(temperature, iterations);

        TSPSolution finalSolution = new TSPSolution(distances.length);

        for (int i = 0; i < iterations; i++) {
            finalSolution = calculateNewSolution(distances, temperature, finalSolution);
            temperature = coolingRate * temperature;
        }


        return finalSolution;
    }

    private TSPSolution calculateNewSolution(double[][] distances, double temperature, TSPSolution finalSolution) {
        TSPSolution temporarySolution = finalSolution.copy();
        temporarySolution.makeSmallChange();
        double temporarySolutionFitness = temporarySolution.calculateFitness(distances);

        double finalSolutionFitness = finalSolution.calculateFitness(distances);

        if (temporarySolutionFitness > finalSolutionFitness) {
            double changeProbability = acceptanceFunction(temporarySolutionFitness, finalSolutionFitness, temperature);

            if (changeProbability > randomGenerator.generateDouble(0, 1)) {
                finalSolution = temporarySolution.copy();
            }

        } else {
            finalSolution = temporarySolution.copy();
        }

        return finalSolution;
    }

    private double acceptanceFunction(double f2, double f1, double temp) {
        double delta = Math.abs(f1 - f2);
        delta = -1 * delta;
        return Math.exp(delta / temp);
    }

    private double calcCR(double temperature, int nIterations) {
        double tIter = 0.001;
        double power = 1.0 / nIterations;
        double tValue = tIter / temperature;

        return Math.pow(tValue, power);
    }

    private double calculateTemp(double[][] d) {
        double averageDistance = getAverage(d) * d.length;
        averageDistance = averageDistance / 2;
        return averageDistance;
    }

    private double getAverage(double[][] array) {
        int counter = 0;
        double sum = 0;
        for (int i = 1; i < array.length; i++) {
            for (int j = i; j < array.length; j++) {
                sum = sum + array[i][j];
                counter++;
            }
        }

        return sum / counter;
    }

}


class RandomHillClimbingAlgorithm {

    public TSPSolution run(double[][] distances, int iterations) {
        System.out.println("Running RMHC");

        TSPSolution currentSolution = new TSPSolution(distances.length);

        for (int i = 0; i < iterations; i++) {
            TSPSolution newSolution = currentSolution.copy();
            newSolution.makeSmallChange();

            double newFitness = newSolution.calculateFitness(distances);
            double currentFitness = currentSolution.calculateFitness(distances);

            if (newFitness < currentFitness) {
                currentSolution = newSolution.copy();
            }

            if (newFitness == 0) {
                currentSolution = newSolution.copy();
                break;
            }


        }

        return currentSolution;
    }

}

