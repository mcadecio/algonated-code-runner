package com.dercio.algonated_code_runner.algorithms.scales;

import java.util.List;

public interface Solution {
    double calculateFitness(List<Double> weights);

    void makeSmallChange();

    List<Integer> getSolution();

    Solution copy();
}
