package com.dercio.algonated_code_runner.replacer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemOutReplacerTest {


    @Test
    void replacesSystemOutWithConsoleLogger() {

        String code = "class Scratch {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Abc\");\n" +
                "    }\n" +
                "}\n";

        SystemOutReplacer replacer = new SystemOutReplacer();
        final String result = replacer.replace(code);

        assertFalse(result.contains("System.out.println"));
        assertTrue(result.contains("consoleLogger."));

    }

    @Test
    void replacesAllSystemOutWithConsoleLogger() {

        String code = "class Scratch {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Abc\");\n" +
                "        System.out.print(\"Abc\");\n" +
                "        System.out.println(\"Abc\");\n" +
                "    }\n" +
                "}\n";

        String expected = "class Scratch {\n" +
                "    public static void main(String[] args) {\n" +
                "        consoleLogger.println(\"Abc\");\n" +
                "        consoleLogger.print(\"Abc\");\n" +
                "        consoleLogger.println(\"Abc\");\n" +
                "    }\n" +
                "}\n";

        SystemOutReplacer replacer = new SystemOutReplacer();
        String result = replacer.replace(code);

        assertFalse(result.contains("System.out.println"));
        assertEquals(expected, result);

    }
}