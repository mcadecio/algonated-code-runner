package com.daio.fyp.algorithms.scales;

import java.util.List;

public interface Algorithm<T> {
    T run(List<Double> weights, int iterations);
}

