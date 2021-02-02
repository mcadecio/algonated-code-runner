package template.scales;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScalesProblem {
    private List<String> solutions;

    // TODO: DO NOT MODIFY THIS METHOD SIGNATURE
    public String runScales(List<Double> weights, int iterations) {

        Algorithm algorithm = new PlaceHolderAlgorithm(); // change this line only to the algorithm you implemented

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

// TODO: Replace this with the algorithm of your choice
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

    // TODO: IMPLEMENT THIS METHOD
    private static String RandomBinaryString(int n) {
        String s = new String();

        //Code goes here
        //Create a random binary string of just ones and zeros of length n

        return (s);
    }

    public ScalesSolution(int n) {
        scasol = RandomBinaryString(n);
    }

    //This is the fitness function for the Scales problem
    //This function returns -1 if the number of weights is less than
    //the size of the current solution
    // TODO: IMPLEMENT THIS METHOD
    public double ScalesFitness(List<Double> weights) {
        if (scasol.length() > weights.size()) return (-1);
        double lhs = 0.0, rhs = 0.0;
        int n = scasol.length();

        //Code goes here

        //Check each element of scasol for a 0 (lhs) and 1 (rhs) add the weight

        //to variables lhs and rhs as appropriate

        return (Math.abs(lhs - rhs));
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

    // TODO: IMPLEMENT THIS METHOD
    public void SmallChange() {

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