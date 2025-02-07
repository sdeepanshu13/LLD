package test;

import main.CronField;
import main.CronFieldType;
import main.CronTableFormatter;
import main.Interfaces.TableFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class TableFormatterTest {
    public static void run() {
        System.out.println("--> [TableFormatterTest] Running Tests...");
        testFormatOutput();
    }

    private static void testFormatOutput() {
        Map<CronFieldType, CronField> fields = new LinkedHashMap<>();
        fields.put(CronFieldType.MINUTE, new CronField(CronFieldType.MINUTE, "*/15"));
        fields.put(CronFieldType.HOUR, new CronField(CronFieldType.HOUR, "0"));
        fields.put(CronFieldType.DAY_OF_MONTH, new CronField(CronFieldType.DAY_OF_MONTH, "1,15"));
        fields.put(CronFieldType.MONTH, new CronField(CronFieldType.MONTH, "*"));
        fields.put(CronFieldType.DAY_OF_WEEK, new CronField(CronFieldType.DAY_OF_WEEK, "1-5"));

        TableFormatter formatter = new CronTableFormatter();

        //  Corrected Expected Output
        String expectedOutput =
                "minute         0 15 30 45\n" +  //  Fixed spacing
                "hour           0\n" +
                "day of month   1 15\n" +
                "month          1 2 3 4 5 6 7 8 9 10 11 12\n" +  //  Fixed formatting
                "day of week    1 2 3 4 5\n" +  //  Fixed formatting
                "command        /usr/bin/find\n";

        String actualOutput = formatter.format(fields, "/usr/bin/find");

        if (!expectedOutput.equals(actualOutput)) {
            throw new AssertionError(" Test Failed: Expected output does not match actual output.\n" +
                                     "Expected:\n" + expectedOutput +
                                     "Actual:\n" + actualOutput);
        }
        System.out.println(" Passed: Table Formatter Output Test");
    }
}
