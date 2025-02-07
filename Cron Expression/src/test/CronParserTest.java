package test;

import main.CronParser;
import main.DefaultCronExpressionValidator;
import main.CronTableFormatter;

public class CronParserTest {
    public static void run() {
        System.out.println("--> [CronParserTest] Running Tests...");
        testValidCronExpression();
        testInvalidCronExpressionTooFewFields();
        testInvalidCronExpressionNullInput();
    }

    private static void testValidCronExpression() {
    try {
        CronParser parser = new CronParser(
            "*/15 0 1,15 * 1-5 /usr/bin/find",
            new DefaultCronExpressionValidator(),
            new CronTableFormatter()
        );

        //  Corrected Expected Output to Match Expanded Values
        String expectedOutput =
                "minute         0 15 30 45\n" +  //  Fixed
                "hour           0\n" +
                "day of month   1 15\n" +
                "month          1 2 3 4 5 6 7 8 9 10 11 12\n" +  //  Fixed
                "day of week    1 2 3 4 5\n" +  //  Fixed
                "command        /usr/bin/find\n";

        String actualOutput = parser.parse();

        if (!expectedOutput.equals(actualOutput)) {
            throw new AssertionError(" Test Failed: Expected output does not match actual output.\n" +
                                     "Expected:\n" + expectedOutput +
                                     "Actual:\n" + actualOutput);
        }
        System.out.println(" Passed: Valid Cron Expression Test");
    } catch (Exception e) {
        System.err.println(" Failed: " + e.getMessage());
    }
}


    private static void testInvalidCronExpressionTooFewFields() {
        try {
            new CronParser(
                "*/15 0 1,15 *",
                new DefaultCronExpressionValidator(),
                new CronTableFormatter() //  Fixed: Passing correct third argument
            );
            throw new AssertionError(" Test Failed: Exception expected but not thrown.");
        } catch (Exception e) {
            System.out.println(" Passed (Caught expected error: " + e.getMessage() + ")");
        }
    }

    private static void testInvalidCronExpressionNullInput() {
        try {
            new CronParser(
                null,
                new DefaultCronExpressionValidator(),
                new CronTableFormatter() // Fixed: Passing correct third argument
            );
            throw new AssertionError(" Test Failed: Exception expected but not thrown.");
        } catch (Exception e) {
            System.out.println(" Passed (Caught expected error: " + e.getMessage() + ")");
        }
    }
}
