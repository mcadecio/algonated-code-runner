package com.daio.fyp.algorithms.scales;

import java.util.List;

public interface Algorithm<T, D> {
    T run(D weights, int iterations);
    List<List<Integer>> getSolutions();
}

