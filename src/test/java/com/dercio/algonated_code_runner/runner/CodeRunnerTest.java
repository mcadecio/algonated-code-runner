package com.dercio.algonated_code_runner.runner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CodeRunnerTest {

    @Nested
    class CompileTest {

        @Test
        @DisplayName("compile should return false when there are imports")
        void compileShouldReturnFalseWhenThereAreImports() {
            CodeOptions options = new CodeOptions()
                    .setCode("import java.applet.Applet;\n" +
                            "\n" +
                            "class Scratch {\n" +
                            "    public static void main(String[] args) {\n" +
                            "            \n" +
                            "    }\n" +
                            "}");


            final CodeRunner codeRunner = new CodeRunner(options);
            boolean isCompileSuccessful = codeRunner.compile();

            assertFalse(isCompileSuccessful);
            assertFalse(codeRunner.isSuccess());
            assertEquals("Please remove the following imports:\n" +
                    "import java.applet.Applet;", codeRunner.getErrorMessage());
        }

        @Test
        @DisplayName("compile should return false when there are illegal methods")
        void compileShouldReturnFalseWhenThereAreIllegalMethods() {
            CodeOptions options = new CodeOptions()
                    .setCode("class Scratch {\n" +
                            "    public static void main(String[] args) {\n" +
                            "          Some.method.call();  \n" +
                            "    }\n" +
                            "}")
                    .setIllegalMethods(Collections.singletonList("Some\\.method\\.call\\(\\);"));

            final CodeRunner codeRunner = new CodeRunner(options);
            boolean isCompileSuccessful = codeRunner.compile();

            assertFalse(isCompileSuccessful);
            assertFalse(codeRunner.isSuccess());
            assertEquals("Please remove the following illegal methods:\n" +
                    "Some.method.call();", codeRunner.getErrorMessage());
        }

        @Test
        @DisplayName("compile should return true when class compiles without any errors")
        void compileShouldReturnTrueWhenClassCompilesWithoutAnyErrors() {
            CodeOptions options = new CodeOptions()
                    .setClassName("Scratch")
                    .setPackageName("com.example")
                    .setIllegalMethods(Collections.emptyList())
                    .setImportsAllowed(Collections.emptyList())
                    .setCode("class Scratch {\n" +
                            "    public void main() {\n" +
                            "          System.out.println(\"Hello, World\");  \n" +
                            "    }\n" +
                            "}");

            final CodeRunner codeRunner = new CodeRunner(options);
            boolean isCompileSuccessful = codeRunner.compile();

            assertTrue(isCompileSuccessful);
        }

        @Test
        @DisplayName("compile should return false when there are class compilation errors")
        void compileShouldReturnFalseWhenThereAreClassCompilationErrors() {
            CodeOptions options = new CodeOptions()
                    .setClassName("Scratch")
                    .setPackageName("com.example")
                    .setIllegalMethods(Collections.emptyList())
                    .setImportsAllowed(Collections.emptyList())
                    // there is a comma missing
                    .setCode("class Scratch {\n" +
                            "    public void main() {\n" +
                            "          System.out.println(\"Hello, World\")  \n" +
                            "    }\n" +
                            "}");

            final CodeRunner codeRunner = new CodeRunner(options);
            boolean isCompileSuccessful = codeRunner.compile();

            assertFalse(isCompileSuccessful);
            assertFalse(codeRunner.isSuccess());
            assertEquals("Compilation error: /com/example/Scratch.java:5: error: ';' expected\n" +
                    "          System.out.println(\"Hello, World\")  \n" +
                    "                                            ^\n" +
                    "1 error\n", codeRunner.getErrorMessage());
        }

        @Test
        @DisplayName("compile should return false when class in the code does not match expected code")
        void compileShouldReturnFalseWhenClassInTheCodeDoesNotMatchExpectedCode() {
            CodeOptions options = new CodeOptions()
                    .setClassName("Scratch")
                    .setPackageName("com.example")
                    .setIllegalMethods(Collections.emptyList())
                    .setImportsAllowed(Collections.emptyList())
                    .setCode("class SomeOtherClass {\n" +
                            "    public void main() {\n" +
                            "          System.out.println(\"Hello, World\");  \n" +
                            "    }\n" +
                            "}");

            final CodeRunner codeRunner = new CodeRunner(options);

            assertThrows(NullPointerException.class, codeRunner::compile);
        }
    }

    @Nested
    class ExecuteTest {

        @Test
        @DisplayName("execute should return an Empty List when called before compile")
        void executeShouldReturnEmptyListWhenCalledBeforeCompile() {
            CodeOptions options = new CodeOptions()
                    .setClassName("Scratch")
                    .setPackageName("com.example")
                    .setIllegalMethods(Collections.emptyList())
                    .setImportsAllowed(Collections.singletonList("import java.util.List;"))
                    .setMethodToCall("aaa")
                    .setIterations(4)
                    .setData(Collections.singletonList(6))
                    .setCode("class Scratch {\n" +
                            "    public int main(List<Double> weights, int iterations) {\n" +
                            "          System.out.println(\"Hello, World\");  \n" +
                            "         return 5;" +
                            "    }\n" +
                            "}");

            final CodeRunner codeRunner = new CodeRunner(options);
            assertTrue(codeRunner.execute().isEmpty());
            assertFalse(codeRunner.isSuccess());
            assertEquals("It seems compile was never called!", codeRunner.getErrorMessage());
        }

        @Test
        @DisplayName("execute should return an empty list when method to call does not exist")
        void executeShouldReturnAnEmptyListWhenMethodToCallDoesNotExist() {
            CodeOptions options = new CodeOptions()
                    .setClassName("Scratch")
                    .setPackageName("com.example")
                    .setIllegalMethods(Collections.emptyList())
                    .setImportsAllowed(Collections.singletonList("import java.util.List;"))
                    // aaa does not exist
                    .setMethodToCall("aaa")
                    .setIterations(4)
                    .setData(Collections.singletonList(6))
                    .setCode("class Scratch {\n" +
                            "    public int main(List<Double> weights, int iterations) {\n" +
                            "          System.out.println(\"Hello, World\");  \n" +
                            "         return 5;" +
                            "    }\n" +
                            "}");

            final CodeRunner codeRunner = new CodeRunner(options);
            codeRunner.compile();
            final List<Integer> returnValue = codeRunner.execute();

            assertTrue(returnValue.isEmpty());
            assertFalse(codeRunner.isSuccess());
            assertTrue(codeRunner.getErrorMessage().startsWith("java.lang.NoSuchMethodException: "));
        }

        @Test
        @DisplayName("execute should return the result of the code run")
        void executeShouldReturnTheResultOfTheCodeRun() {
            CodeOptions options = new CodeOptions()
                    .setClassName("Scratch")
                    .setPackageName("com.example")
                    .setIllegalMethods(Collections.emptyList())
                    .setImportsAllowed(Arrays.asList("import java.util.Collections;", "import java.util.List;"))
                    .setMethodToCall("main")
                    .setIterations(4)
                    .setData(Collections.singletonList(6))
                    .setCode("class Scratch {\n" +
                            "    public List<Integer> main(List<Double> weights, int iterations) {\n" +
                            "          System.out.println(\"Hello, World\");  \n" +
                            "         return Collections.singletonList(5);" +
                            "    }\n" +
                            "}");

            final CodeRunner codeRunner = new CodeRunner(options);
            codeRunner.compile();
            final List<Integer> returnValue = codeRunner.execute();


            assertEquals(Collections.singletonList(5), returnValue);
            assertTrue(codeRunner.isSuccess());
            assertEquals("Compile and Run was a success!", codeRunner.getErrorMessage());
        }

    }

}