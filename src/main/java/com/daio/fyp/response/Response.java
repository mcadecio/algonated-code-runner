package com.daio.fyp.response;

import com.daio.fyp.runner.CodeRunnerSummary;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class Response {

    private boolean isSuccess;
    private String consoleOutput;
    private List<Integer> result;
    private Object data;
    private CodeRunnerSummary summary;
    private List<List<Integer>> solutions = Collections.emptyList();

    @JsonProperty("isSuccess")
    public boolean isSuccess() {
        return isSuccess;
    }

    public Response setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    public String getConsoleOutput() {
        return consoleOutput;
    }

    public Response setConsoleOutput(String consoleOutput) {
        this.consoleOutput = consoleOutput;
        return this;
    }

    public List<Integer> getResult() {
        return result;
    }

    public Response setResult(List<Integer> result) {
        this.result = result;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Response setData(Object data) {
        this.data = data;
        return this;
    }

    public CodeRunnerSummary getSummary() {
        return summary;
    }

    public Response setSummary(CodeRunnerSummary summary) {
        this.summary = summary;
        return this;
    }

    public List<List<Integer>> getSolutions() {
        return solutions;
    }

    public Response setSolutions(List<List<Integer>> solutions) {
        this.solutions = solutions;
        return this;
    }
}
