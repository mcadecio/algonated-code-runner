package com.dercio.algonated_code_runner.runner;

import com.dercio.algonated_code_runner.verifier.ImportVerifier;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImportVerifierTest {

    private static final ImportVerifier verifier = new ImportVerifier();

    @Test
    void returnsAListWhenImportsWereFound() {
        String code = "import java.util.List;\n" +
                "import java.util.regex.Matcher;\n" +
                "import java.util.regex.Pattern;\n" +
                "class ImportVerifierTest {" +
                "}";

        final List<String> importsFound = verifier.verify(code);

        assertEquals(3, importsFound.size());
        assertTrue(importsFound.contains("import java.util.List;"));
        assertTrue(importsFound.contains("import java.util.regex.Matcher;"));
        assertTrue(importsFound.contains("import java.util.regex.Pattern;"));
    }

    @Test
    void returnsAnEmptyListWhenNoImportsWereFound() {
        String code = "class ImportVerifierTest {" +
                "}";

        final List<String> importsFound = verifier.verify(code);

        assertTrue(importsFound.isEmpty());
    }
}