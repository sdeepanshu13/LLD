package com.models;

import com.enums.CronFieldType;
import com.exception.InvalidCronException;
import com.Interfaces.CronExpressionValidator;
import com.Interfaces.TableFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;



import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ModelsTests {
    private final CronExpressionValidator validator = new DefaultCronExpressionValidator();
    private final TableFormatter formatter = new CronTableFormatter();

    // Original individual tests

    @Test
    public void testValidCronFieldParsing() {
        CronField minuteField = new CronField(CronFieldType.MINUTE, "*/15");
        assertEquals("0 15 30 45", minuteField.getFormattedValues());

        CronField hourField = new CronField(CronFieldType.HOUR, "1-5");
        assertEquals("1 2 3 4 5", hourField.getFormattedValues());

        CronField dayOfMonthField = new CronField(CronFieldType.DAY_OF_MONTH, "1,15");
        assertEquals("1 15", dayOfMonthField.getFormattedValues());
    }

    @Test
    public void testInvalidCronFieldParsing() {
        assertThrows(InvalidCronException.class, () -> new CronField(CronFieldType.MINUTE, "*/abc"));
    }

    @Test
    public void testInvalidRange() {
        assertThrows(InvalidCronException.class, () -> new CronField(CronFieldType.HOUR, "25-30"));
    }

    @Test
    public void testInvalidStepValue() {
        assertThrows(InvalidCronException.class, () -> new CronField(CronFieldType.DAY_OF_MONTH, "*/0"));
    }

    @Test
    public void testCronParserWithValidExpression() {
        CronParser parser = new CronParser("*/15 0 1,15 * 1-5 /usr/bin/find", validator, formatter);
        String output = parser.parse();
        assertNotNull(output);
    }

    @Test
    public void testCronParserWithInvalidExpression() {
        // With the updated CronParser, this instantiation will now throw an exception immediately.
        assertThrows(InvalidCronException.class, () ->
            new CronParser("*/15 0 1,15 # 1-59 /usr/bin/find", validator, formatter)
        );
    }

    @Test
    public void testCronParserWithIncompleteExpression() {
        assertThrows(InvalidCronException.class, () ->
            new CronParser("*/15 0 1,15 *", validator, formatter)
        );
    }

    @Test
    public void testCronTableFormatter() {
        CronParser parser = new CronParser("*/15 0 1,15 * 1-5 /usr/bin/find", validator, formatter);
        String output = parser.parse();
        assertTrue(output.contains("minute"));
        assertTrue(output.contains("hour"));
        assertTrue(output.contains("day of month"));
        assertTrue(output.contains("month"));
        assertTrue(output.contains("day of week"));
        assertTrue(output.contains("command"));
    }

    @Test
    public void testCronParserInvalidCommand() {
        assertThrows(InvalidCronException.class, () ->
            new CronParser("*/15 0 1,15 * 1-5 !@#$%^&*", validator, formatter)
        );
    }

    @Test
    public void testEmptyCronExpression() {
        assertThrows(InvalidCronException.class, () -> validator.validate(""));
    }

    @Test
    public void testNullCronExpression() {
        assertThrows(InvalidCronException.class, () -> validator.validate(null));
    }

    @Test
    public void testInvalidFieldValues() {
        assertThrows(InvalidCronException.class, () -> new CronField(CronFieldType.MINUTE, "61"));
    }

    // Parameterized test for multiple cron expressions

    static Stream<Arguments> cronExpressions() {
        return Stream.of(
            // Expression, Expected Validity
            Arguments.of("*/15 0 1,32 * 1-5 /usr/bin/find", false),  // Invalid: Day of month 32
            Arguments.of("*/20 5 1,20 * 1-5 /usr/bin/find", true),     // Valid
            Arguments.of("60 0 1,15 * 1-5 /usr/bin/find", false),       // Invalid: Minute 60
            Arguments.of("*/10 25 1,15 * 1-5 /usr/bin/find", false),     // Invalid: Hour 25
            Arguments.of("*/5 12 1-5 0-15 1-5 /usr/bin/find", false),    // Invalid: Month 0-15
            Arguments.of("*/5 12 1-5 * 10 /usr/bin/find", false),        // Invalid: Day of week 10
            Arguments.of("*/5 * * * * /usr/bin/find", true),             // Valid: Runs every 5 minutes
            Arguments.of("*/5 * * * * ", false),                         // Invalid: Command is missing
            Arguments.of("*/5 * * * * 123invalid?", false),              // Invalid: Command contains special characters
            Arguments.of("*/5 * * * * ls -la /home/user", true)           // Valid: Command with arguments
        );
    }

    @ParameterizedTest
    @MethodSource("cronExpressions")
    public void testMultipleCronExpressions(String cronExpression, boolean isValid) {
        if (isValid) {
            // Expect the expression to be valid.
            CronParser parser = new CronParser(cronExpression, validator, formatter);
            String output = parser.parse();
            assertNotNull(output, "Output should not be null for valid expression: " + cronExpression);
            System.out.println("Valid expression: " + cronExpression + "\nOutput:\n" + output);
        } else {
            // Expect the expression to throw an exception.
            assertThrows(InvalidCronException.class, () -> new CronParser(cronExpression, validator, formatter),
                "Expected InvalidCronException for expression: " + cronExpression);
            System.out.println("Invalid expression (as expected): " + cronExpression);
        }
    }
}
