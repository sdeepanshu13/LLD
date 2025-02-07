package test;

import main.CronField;
import main.CronFieldType;
import main.exception.InvalidCronException;

public class CronFieldTest {
    public static void run() {
        System.out.println("--> [CronFieldTest] Running Tests...");
        testValidMinuteField();
        testValidHourField();
        testValidDayOfWeekField();
        testValidMonthField();
        testValidDayOfMonthField();
        testInvalidRange();
        testInvalidValueOutOfBounds();
    }

    private static void testValidMinuteField() {
        CronField field = new CronField(CronFieldType.MINUTE, "*/15");
        String expected = "0 15 30 45";
        if (!expected.equals(field.getFormattedValues())) {
            throw new AssertionError(" Test Failed: Expected " + expected + " but got " + field.getFormattedValues());
        }
        System.out.println(" Passed: Valid Minute Field");
    }

    private static void testValidHourField() {
        CronField field = new CronField(CronFieldType.HOUR, "1,5,10");
        String expected = "1 5 10";
        if (!expected.equals(field.getFormattedValues())) {
            throw new AssertionError(" Test Failed: Expected " + expected + " but got " + field.getFormattedValues());
        }
        System.out.println(" Passed: Valid Hour Field");
    }

    private static void testValidDayOfWeekField() {
        CronField field = new CronField(CronFieldType.DAY_OF_WEEK, "1-5");
        String expected = "1 2 3 4 5";
        if (!expected.equals(field.getFormattedValues())) {
            throw new AssertionError(" Test Failed: Expected " + expected + " but got " + field.getFormattedValues());
        }
        System.out.println(" Passed: Valid Day of Week Field");
    }

    private static void testValidMonthField() {
        CronField field = new CronField(CronFieldType.MONTH, "1,6,12");
        String expected = "1 6 12";
        if (!expected.equals(field.getFormattedValues())) {
            throw new AssertionError(" Test Failed: Expected " + expected + " but got " + field.getFormattedValues());
        }
        System.out.println(" Passed: Valid Month Field");
    }

    private static void testValidDayOfMonthField() {
        CronField field = new CronField(CronFieldType.DAY_OF_MONTH, "1,15,30");
        String expected = "1 15 30";
        if (!expected.equals(field.getFormattedValues())) {
            throw new AssertionError(" Test Failed: Expected " + expected + " but got " + field.getFormattedValues());
        }
        System.out.println(" Passed: Valid Day of Month Field");
    }

    private static void testInvalidRange() {
        try {
            new CronField(CronFieldType.MINUTE, "50-10");
            throw new AssertionError(" Test Failed: Exception expected but not thrown.");
        } catch (InvalidCronException e) {
            System.out.println(" Passed: Invalid Range Test (Caught expected error: " + e.getMessage() + ")");
        }
    }

    private static void testInvalidValueOutOfBounds() {
        try {
            new CronField(CronFieldType.HOUR, "25");
            throw new AssertionError(" Test Failed: Exception expected but not thrown.");
        } catch (InvalidCronException e) {
            System.out.println(" Passed: Invalid Value Test (Caught expected error: " + e.getMessage() + ")");
        }
    }
}
