package com.daio.fyp.response;

import com.daio.fyp.runner.CodeRunnerSummary;

import java.util.List;

public class Response {

    private boolean isSuccess;
    private String consoleOutput;
    private List<Integer> result;
    private Object data;
    private CodeRunnerSummary summary;

    public Response(
            boolean isSuccess,
            String consoleOutput,
            List<Integer> result,
            Object data,
            CodeRunnerSummary summary) {
        this.isSuccess = isSuccess;
        this.consoleOutput = consoleOutput;
        this.result = result;
        this.data = data;
        this.summary = summary;
    }


}
