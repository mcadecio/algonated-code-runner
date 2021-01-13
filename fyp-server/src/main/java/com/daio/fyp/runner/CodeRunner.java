package com.daio.fyp.runner;

import com.daio.fyp.verifier.IllegalMethodVerifier;
import com.daio.fyp.verifier.ImportVerifier;
import com.daio.fyp.verifier.Verifier;
import org.joor.Reflect;
import org.joor.ReflectException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CodeRunner {

    private static final String PLEASE_REMOVE = "Please remove the following ";

    private String errorMessage;
    private boolean isSuccess;
    private Reflect compiledClass;
    private final CodeOptions options;

    public CodeRunner(CodeOptions options) {
        this.options = options;
        this.isSuccess = false;
        this.errorMessage = "It seems compile was never called!";
    }

    public boolean compile() {
        if (verifyError(new ImportVerifier(),"imports:\n" )) {
            return false;
        }

        if (verifyError(new IllegalMethodVerifier(options.getIllegalMethods()),"illegal methods:\n" )) {
            return false;
        }

        try {
            compiledClass = compileClass();
            isSuccess = true;
            errorMessage = "";
        } catch (ReflectException reflectException) {
            handleError(reflectException.getMessage());
            return false;
        }

        return true;
    }

    public List<Integer> execute() {

        if (!isSuccess) {
            return Collections.emptyList();
        }

        final List<Double> data = options.getData()
                .stream()
                .map(Double::valueOf)
                .collect(Collectors.toList());

        try {
            final List<Integer> valueToReturn = compiledClass.call(options.getMethodToCall(), data, options.getIterations())
                    .get();
            isSuccess = true;
            return valueToReturn;
        } catch (ReflectException reflectException) {
            handleError(reflectException.getMessage());
            return Collections.emptyList();
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    private void handleError(String errorMessage) {
        this.errorMessage = errorMessage;
        isSuccess = false;
    }

    private boolean verifyError(Verifier verifier, String errorMessage) {
        final List<String> errors = verifier.verify(options.getCode());
        if (!errors.isEmpty()) {
            handleError(PLEASE_REMOVE + errorMessage + String.join("\n", errors));
            return true;
        }
        return false;
    }

    private Reflect compileClass() {
        String packageName = "package " + options.getPackageName() + ";";
        String className = options.getPackageName() + "." + options.getClassName();
        return Reflect.compile(className, packageName +
                "\n" +
                String.join("\n", options.getImportsAllowed()) +
                "\n" +
                options.getCode()
        ).create();
    }
}
