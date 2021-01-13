package com.daio.fyp.runner;

import java.util.List;

public class CodeOptions {

    private String className;
    private String packageName;
    private String methodToCall;
    private int iterations;
    private List<String> importsAllowed;
    private List<String> illegalMethods;
    private List<Integer> data;
    private String code;

    public String getClassName() {
        return className;
    }

    public CodeOptions setClassName(String className) {
        this.className = className;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public CodeOptions setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public String getMethodToCall() {
        return methodToCall;
    }

    public CodeOptions setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
        return this;
    }

    public int getIterations() {
        return iterations;
    }

    public CodeOptions setIterations(int iterations) {
        this.iterations = iterations;
        return this;
    }

    public List<String> getImportsAllowed() {
        return importsAllowed;
    }

    public CodeOptions setImportsAllowed(List<String> importsAllowed) {
        this.importsAllowed = importsAllowed;
        return this;
    }

    public List<String> getIllegalMethods() {
        return illegalMethods;
    }

    public CodeOptions setIllegalMethods(List<String> illegalMethods) {
        this.illegalMethods = illegalMethods;
        return this;
    }

    public List<Integer> getData() {
        return data;
    }

    public CodeOptions setData(List<Integer> data) {
        this.data = data;
        return this;
    }

    public String getCode() {
        return code;
    }

    public CodeOptions setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String toString() {
        return "CodeOptions{" +
                "className='" + className + '\'' +
                ", packageName='" + packageName + '\'' +
                ", methodToCall='" + methodToCall + '\'' +
                ", iterations=" + iterations +
                ", importsAllowed=" + importsAllowed +
                ", illegalMethods=" + illegalMethods +
                ", data=" + data +
                ", code='" + code + '\'' +
                '}';
    }
}
