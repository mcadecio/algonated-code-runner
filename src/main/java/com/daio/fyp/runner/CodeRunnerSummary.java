package com.daio.fyp.runner;

public class CodeRunnerSummary {

    private int iterations;
    private double fitness;
    private double timeRun;
    private double efficacy;

    public int getIterations() {
        return iterations;
    }

    public CodeRunnerSummary setIterations(int iterations) {
        this.iterations = iterations;
        return this;
    }

    public double getFitness() {
        return fitness;
    }

    public CodeRunnerSummary setFitness(double fitness) {
        this.fitness = fitness;
        return this;
    }

    public double getTimeRun() {
        return timeRun;
    }

    public CodeRunnerSummary setTimeRun(double timeRun) {
        this.timeRun = timeRun;
        return this;
    }

    public double getEfficacy() {
        return efficacy;
    }

    public CodeRunnerSummary setEfficacy(double efficacy) {
        this.efficacy = efficacy;
        return this;
    }
}
