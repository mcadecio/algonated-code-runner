package com.dercio.algonated_code_runner.random;

import java.util.Random;

public class UniformRandomGenerator {
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
