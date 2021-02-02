package old.scales;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScalesProblem {
    private List<String> solutions;

    public String runScales(List<Double> weights, int iterations) {

        Algorithm algorithm = new SimulatedAnnealingAlgorithm(); // change this line only to the algorithm you implemented

        ScalesSolution solution = algorithm.run(weights, iterations);
        solutions = algorithm.getSolutions();
        return solution.GetSol();
    }

}

/**
 * The methods your Algorithm should implement
 */
interface Algorithm {

    ScalesSolution run(List<Double> weights, int iterations);

    List<String> getSolutions();
}

// Replace this with the algorithm of your choice
class PlaceHolderAlgorithm implements Algorithm {

    @Override
    public ScalesSolution run(List<Double> weights, int iterations) {
        return new ScalesSolution(10);
    }

    @Override
    public List<String> getSolutions() {
        return new ArrayList<>();
    }
}

class RandomHillClimbingAlgorithms implements Algorithm {

    private final List<String> solutions = new ArrayList<>();

    @Override
    public ScalesSolution run(List<Double> weights, int iterations) {
        System.out.println("Running RMHC");

        ScalesSolution currentSolution = new ScalesSolution(weights.size());

        for (int i = 0; i < iterations; i++) {
            ScalesSolution newSolution = currentSolution.copy();
            newSolution.SmallChange();

            double newFitness = newSolution.ScalesFitness(weights);
            double currentFitness = currentSolution.ScalesFitness(weights);

            if (newFitness < currentFitness) {
                currentSolution = newSolution.copy();
            }

            if (newFitness == 0) {
                currentSolution = newSolution.copy();
                solutions.add(currentSolution.GetSol());
                break;
            }

            solutions.add(currentSolution.GetSol());
        }

        return currentSolution;
    }

    @Override
    public List<String> getSolutions() {
        return solutions;
    }
}

class SimulatedAnnealingAlgorithm implements Algorithm {

    private final List<String> solutions = new ArrayList<>();

    @Override
    public ScalesSolution run(List<Double> weights, int iterations) {
        System.out.println("Running SA");

        double temperature = 1000;
        double coolingRate = calcCR(temperature, iterations);

        ScalesSolution finalSolution = new ScalesSolution(weights.size());

        for (int i = 0; i < iterations; i++) {
            finalSolution = calculateNewSolution(weights, temperature, finalSolution);
            temperature = coolingRate * temperature;
            solutions.add(finalSolution.GetSol());
        }


        return finalSolution;
    }

    @Override
    public List<String> getSolutions() {
        return solutions;
    }

    private ScalesSolution calculateNewSolution(List<Double> weights, double temperature, ScalesSolution finalSolution) {
        ScalesSolution temporarySolution = finalSolution.copy();
        temporarySolution.SmallChange();
        double temporarySolutionFitness = temporarySolution.ScalesFitness(weights);

        double finalSolutionFitness = finalSolution.ScalesFitness(weights);

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

}

class ScalesSolution {
    private String scasol; //Creates a new scales solution based on a string parameter

    //The string parameter is checked to see if it contains all zeros and ones
    //Otherwise the random binary string generator is used (n = length of parameter)
    public ScalesSolution(String s) {
        boolean ok = true;
        int n = s.length();
        for (int i = 0; i < n; ++i) {
            char si = s.charAt(i);
            if (si != '0' && si != '1') ok = false;
        }

        if (ok) {
            scasol = s;
        } else {
            scasol = RandomBinaryString(n);
        }
    }

    private static String RandomBinaryString(int n) {
        String s = new String();

        for (int i = 0; i < n; i++) {
            String randomChar = String.valueOf(CS2004.UI(0, 1));
            if (randomChar.equals("0")) {
                s = s.concat(randomChar);
            } else {
                s = randomChar.concat(s);
            }
        }


        return (s);
    }

    public ScalesSolution(int n) {
        scasol = RandomBinaryString(n);
    }

    //This is the fitness function for the Scales problem
    //This function returns -1 if the number of weights is less than
    //the size of the current solution
    public double ScalesFitness(List<Double> weights) {
        if (scasol.length() > weights.size()) return (-1);
        double leftHandSide = 0.0;
        double rightHandSide = 0.0;
        int n = scasol.length();

        for (int i = 0; i < n; i++) {
            if (scasol.charAt(i) == '0') {
                leftHandSide += weights.get(i);
            } else
                rightHandSide += weights.get(i);
        }

        return (Math.abs(leftHandSide - rightHandSide));
    }

    /**
     * Display the string without a new line
     */
    public void print() {
        System.out.print(scasol);
    }

    //Display the string with a new line
    public void println() {
        print();
        System.out.println();
    }

    public String GetSol() {
        return scasol;
    }

    public void SmallChange() {
        int length = scasol.length();

        int randomInt = CS2004.UI(0, length - 1);
        char targetChar = scasol.charAt(randomInt);
        StringBuilder strBuilder = new StringBuilder(scasol);

        if (targetChar == '1') {
            strBuilder.replace(randomInt, randomInt + 1, "0");
        } else {
            strBuilder.replace(randomInt, randomInt + 1, "1");
        }

        scasol = strBuilder.toString();
    }

    public ScalesSolution copy() {
        return new ScalesSolution(scasol);
    }
}


//Some useful code that we will probably reuse in later laboratories...
class CS2004 {
    //Shared random object
    static private Random rand;

    // Create a uniformly distributed random integer between aa and bb inclusive
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

    //This method reads in a text file and parses all of the numbers in it
    //This code is not very good and can be improved!
    //But it should work!!!
    //It takes in as input a string filename and returns an array list of Doubles
    static public ArrayList<Double> ReadNumberFile(String filename) {
        ArrayList<Double> res = new ArrayList<Double>();
        Reader r;

        try {
            r = new BufferedReader(new FileReader(filename));
            StreamTokenizer stok = new StreamTokenizer(r);
            stok.parseNumbers();
            stok.nextToken();
            while (stok.ttype != StreamTokenizer.TT_EOF) {
                if (stok.ttype == StreamTokenizer.TT_NUMBER) {
                    res.add(stok.nval);
                }
                stok.nextToken();
            }
        } catch (Exception E) {
            System.out.println("+++ReadFile: " + E.getMessage());
        }

        return (res);
    }
}