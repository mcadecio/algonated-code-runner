package template.tsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TSPProblem {
    private List<List<Integer>> solutions;

    // TODO: DO NOT MODIFY THIS METHOD SIGNATURE
    public List<Integer> runTSP(double[][] distances, int iterations) {

        Algorithm algorithm = new PlaceHolderAlgorithm(); // change this line only to the algorithm you implemented

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

// TODO: Replace this with the algorithm of your choice
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

class TSPSolution {
    private final List<Integer> solution;

    public TSPSolution(int length) {
        this(generateRandomList(length));
    }

    public TSPSolution(List<Integer> solution) {
        this.solution = solution;
    }

    // TODO: IMPLEMENT THIS METHOD
    public double calculateFitness(double[][] distances) {
        return -1;
    }

    // TODO: IMPLEMENT THIS METHOD
    public void makeSmallChange() {

    }

    public List<Integer> getSolution() {
        return solution;
    }

    public TSPSolution copy() {
        return new TSPSolution(new ArrayList<>(solution));
    }

    // TODO: IMPLEMENT THIS METHOD
    private static List<Integer> generateRandomList(int size) {
        return Collections.emptyList();
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
