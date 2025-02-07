package test;

public class TestRunner {
    public static void runTests() {
        System.out.println("\n /////// Running Tests... ////////\n");

        CronParserTest.run();
        System.out.println("\n");
        CronExpressionValidatorTest.run();
        System.out.println("\n");
        CronFieldTest.run();
        System.out.println("\n");
        TableFormatterTest.run();
        System.out.println("\n");
        System.out.println("\n ***** All tests executed! *****");
    }
}
