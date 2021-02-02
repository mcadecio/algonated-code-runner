package old.tsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TSPProblem {
    private List<List<Integer>> solutions;

    public List<Integer> runTSP(double[][] distances, int iterations) {

        Algorithm algorithm = new SimulatedAnnealingAlgorithm(); // change this line only to the algorithm you implemented

        TSPSolution solution = algorithm.run(distances, iterations);
        solutions = algorithm.getSolutions();
        return solution.getSolution();
    }

}

/**
 * The methods your Algorithm should implement
 */
interface Algorithm {

    TSPSolution run(double[][] distances, int iterations);

    List<List<Integer>> getSolutions();
}

// Replace this with the algorithm of your choice
class PlaceHolderAlgorithm implements Algorithm {

    private List<List<Integer>> solutions = new ArrayList<>();

    @Override
    public TSPSolution run(double[][] distances, int iterations) {
        return new TSPSolution(10);
    }

    @Override
    public List<List<Integer>> getSolutions() {
        return solutions;
    }
}

class SimulatedAnnealingAlgorithm implements Algorithm {

    private List<List<Integer>> solutions = new ArrayList<>();

    public TSPSolution run(double[][] distances, int iterations) {
        System.out.println("Running SA");

        double temperature = calculateTemp(distances);
        double coolingRate = calcCR(temperature, iterations);

        TSPSolution finalSolution = new TSPSolution(distances.length);

        for (int i = 0; i < iterations; i++) {
            finalSolution = calculateNewSolution(distances, temperature, finalSolution);
            temperature = coolingRate * temperature;
            solutions.add(finalSolution.getSolution());
        }

        return finalSolution;
    }

    @Override
    public List<List<Integer>> getSolutions() {
        return solutions;
    }

    private TSPSolution calculateNewSolution(double[][] distances, double temperature, TSPSolution finalSolution) {
        TSPSolution temporarySolution = finalSolution.copy();
        temporarySolution.makeSmallChange();
        double temporarySolutionFitness = temporarySolution.calculateFitness(distances);

        double finalSolutionFitness = finalSolution.calculateFitness(distances);

        if (temporarySolutionFitness > finalSolutionFitness) {
            double changeProbability = acceptanceFunction(temporarySolutionFitness, finalSolutionFitness, temperature);

            if (changeProbability > CS2004.UR(0, 1)) {
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


class RandomHillClimbingAlgorithm implements Algorithm {

    private List<List<Integer>> solutions = new ArrayList<>();

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
                solutions.add(currentSolution.getSolution());
                break;
            }

            solutions.add(currentSolution.getSolution());
        }

        return currentSolution;
    }

    @Override
    public List<List<Integer>> getSolutions() {
        return solutions;
    }
}

class TSPSolution {
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
            firstCity = CS2004.UI(0, solution.size() - 1);
            secondCity =  CS2004.UI(0, solution.size() - 1);
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
            int randomIndex = CS2004.UI(0, orderedList.size() - 1);
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

//Some useful code that we will probably reuse in later laboratories...
class CS2004 {
    static public double[][] ReadArrayFile(String filename, String sep) {
        double res[][] = null;
        try {
            BufferedReader input = null;
            input = new BufferedReader(new FileReader(filename));
            String line = null;
            int ncol = 0;
            int nrow = 0;

            while ((line = input.readLine()) != null) {
                ++nrow;
                String[] columns = line.split(sep);
                ncol = Math.max(ncol, columns.length);
            }
            res = new double[nrow][ncol];
            input = new BufferedReader(new FileReader(filename));
            int i = 0, j = 0;
            while ((line = input.readLine()) != null) {

                String[] columns = line.split(sep);
                for (j = 0; j < columns.length; ++j) {
                    res[i][j] = Double.parseDouble(columns[j]);
                }
                ++i;
            }
        } catch (Exception E) {
            System.out.println("+++ReadArrayFile: " + E.getMessage());
        }
        return (res);
    }

    //Shared random object
    static private Random rand;

    //Create a uniformly distributed random integer between aa and bb inclusive
    static public int UI(int aa, int bb) {
        int a = Math.min(aa, bb);
        int b = Math.max(aa, bb);
        if (rand == null) {
            rand = new Random();
            rand.setSeed(System.nanoTime());
        }
        int d = b - a + 1;
        int x = rand.nextInt(d) + a;
        return (x);
    }

    //Create a uniformly distributed random double between a and b inclusive
    static public double UR(double a, double b) {
        if (rand == null) {
            rand = new Random();
            rand.setSeed(System.nanoTime());
        }
        return ((b - a) * rand.nextDouble() + a);
    }
}
