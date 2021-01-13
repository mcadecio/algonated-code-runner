package com.daio.fyp.response;

import java.util.List;

public class Response {

    private boolean isSuccess;
    private String consoleOutput;
    private List<Integer> result;
    private List<Integer> data;

    public Response(boolean isSuccess, String consoleOutput, List<Integer> result, List<Integer> data) {
        this.isSuccess = isSuccess;
        this.consoleOutput = consoleOutput;
        this.result = result;
        this.data = data;
    }


}
