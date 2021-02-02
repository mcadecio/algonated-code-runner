package com.daio.fyp.runner.demo;

public class DemoRequest<T> {

    private String problem;
    private String algorithm;
    private T data;
    private int iterations;

    public String getProblem() {
        return problem;
    }

    public DemoRequest<T> setProblem(String problem) {
        this.problem = problem;
        return this;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public DemoRequest<T> setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public T getData() {
        return data;
    }

    public DemoRequest<T> setData(T data) {
        this.data = data;
        return this;
    }

    public int getIterations() {
        return iterations;
    }

    public DemoRequest<T> setIterations(int iterations) {
        this.iterations = iterations;
        return this;
    }
}
