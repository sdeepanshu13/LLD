package com.models;

import com.enums.CronFieldType;
import com.Interfaces.CronExpressionValidator;
import com.Interfaces.TableFormatter;
import com.exception.InvalidCronException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CronParser {
    private final String cronExpression;
    private final TableFormatter formatter;
    private final String command;

    public CronParser(String cronExpression, CronExpressionValidator validator, TableFormatter formatter) {
        this.cronExpression = cronExpression;
        this.formatter = formatter;

        // Validate the overall cron expression structure
        validator.validate(cronExpression);

        // Split cron fields and extract command
        String[] parts = cronExpression.trim().split("\\s+");
        if (parts.length < 6) {
            throw new InvalidCronException("Cron expression must contain 5 time fields + a command.");
        }

        // Extract and validate command
        this.command = extractCommand(parts);
        validateCommand(this.command);

        // Force immediate validation of each time field.
        // This ensures that any invalid field (like '#' in the month field) throws an exception upon instantiation.
        new CronField(CronFieldType.MINUTE, parts[0]);
        new CronField(CronFieldType.HOUR, parts[1]);
        new CronField(CronFieldType.DAY_OF_MONTH, parts[2]);
        new CronField(CronFieldType.MONTH, parts[3]); // Will throw an exception if invalid.
        new CronField(CronFieldType.DAY_OF_WEEK, parts[4]);
    }

    private String extractCommand(String[] parts) {
        if (parts.length <= 5) {
            throw new InvalidCronException("Command field cannot be empty.");
        }
        return String.join(" ", java.util.Arrays.copyOfRange(parts, 5, parts.length)).trim();
    }

    private void validateCommand(String command) {
        if (command.isEmpty()) {
            throw new InvalidCronException("Command field cannot be empty.");
        }
        // Optional: Validate command format with a regex.
        Pattern validCommandPattern = Pattern.compile("^[a-zA-Z0-9_/\\.\\-]+(\\s+[a-zA-Z0-9_/\\.\\-]+)*$");
        if (!validCommandPattern.matcher(command).matches()) {
            throw new InvalidCronException("Invalid command format: " + command);
        }
    }

    public String parse() {
        // Create a map of CronFieldType to CronField.
        Map<CronFieldType, CronField> fields = new LinkedHashMap<>();
        String[] parts = cronExpression.split("\\s+");

        fields.put(CronFieldType.MINUTE, new CronField(CronFieldType.MINUTE, parts[0]));
        fields.put(CronFieldType.HOUR, new CronField(CronFieldType.HOUR, parts[1]));
        fields.put(CronFieldType.DAY_OF_MONTH, new CronField(CronFieldType.DAY_OF_MONTH, parts[2]));
        fields.put(CronFieldType.MONTH, new CronField(CronFieldType.MONTH, parts[3]));
        fields.put(CronFieldType.DAY_OF_WEEK, new CronField(CronFieldType.DAY_OF_WEEK, parts[4]));

        return formatter.format(fields, command);
    }
}
