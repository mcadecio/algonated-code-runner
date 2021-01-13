package com.daio.fyp.runner;

import com.daio.fyp.verifier.IllegalMethodVerifier;
import com.daio.fyp.verifier.Verifier;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IllegalMethodVerifierTest {


    @Test
    void returnsAListWhenIllegalMethodsWereFound() {
        Verifier verifier = new IllegalMethodVerifier(Collections.singletonList("System\\.exit\\(.*\\);"));
        String code = "class Scratch {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.exit(10);\n" +
                "        System.exit(0);\n" +
                "        System.exit(11);\n" +
                "    }\n" +
                "}";

        final List<String> methodsFound = verifier.verify(code);

        assertEquals(3, methodsFound.size());
        assertTrue(methodsFound.contains("System.exit(10);"));
        assertTrue(methodsFound.contains("System.exit(0);"));
        assertTrue(methodsFound.contains("System.exit(11);"));
    }

    @Test
    void returnsAListWhenIllegalMethodsWereFoundForMultipleIllegalMethods() {
        Verifier verifier = new IllegalMethodVerifier(
                Arrays.asList("System\\.exit\\(.*\\);", "System\\.out\\.println\\(.*\\);")
        );
        String code = "class Scratch {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.exit(10);\n" +
                "        System.exit(0);\n" +
                "        System.exit(11);\n" +
                "        System.out.println();\n" +
                "    }\n" +
                "}";

        final List<String> methodsFound = verifier.verify(code);

        assertEquals(4, methodsFound.size());
        assertTrue(methodsFound.contains("System.exit(10);"));
        assertTrue(methodsFound.contains("System.exit(0);"));
        assertTrue(methodsFound.contains("System.exit(11);"));
        assertTrue(methodsFound.contains("System.out.println();"));
    }

    @Test
    void returnsAnEmptyListWhenNoIllegalMethodsWereFound() {
        Verifier verifier = new IllegalMethodVerifier(Collections.singletonList("System\\.exit\\(.*\\);"));
        String code = "class Scratch {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println();\n" +
                "    }\n" +
                "}";

        final List<String> importsFound = verifier.verify(code);

        assertTrue(importsFound.isEmpty());
    }

    @Test
    void returnsAnEmptyListWhenNoIllegalMethodsIsEmpty() {
        Verifier verifier = new IllegalMethodVerifier(Collections.emptyList());
        String code = "class Scratch {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println();\n" +
                "    }\n" +
                "}";

        final List<String> importsFound = verifier.verify(code);

        assertTrue(importsFound.isEmpty());
    }


}