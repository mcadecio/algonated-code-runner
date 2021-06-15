package com.dercio.algonated_code_runner.runner.calculator;

import java.util.List;

public interface Calculator<T> {

    double calculate(T data, List<Integer> solution);
}
