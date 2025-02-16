package main;

import main.Interfaces.CronFieldParser;
import main.exception.InvalidCronException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a single cron field and parses it.
 */
public class CronField implements CronFieldParser {
    private final CronFieldType fieldType;
    private final String values;

    public CronField(CronFieldType fieldType, String fieldExpression) {
        this.fieldType = fieldType;
        this.values = parseField(fieldExpression);
    }

    @Override
    public String parseField(String fieldExpression) {
        Set<Integer> result = new TreeSet<>();

        for (String part : fieldExpression.split(",")) {
            part = part.trim();
            if (part.equals("*")) {
                // Expand full range
                for (int i = fieldType.getMin(); i <= fieldType.getMax(); i++) result.add(i);
            } else if (part.contains("-")) {
                // Handle ranges (e.g., "1-5")
                String[] range = part.split("-");
                if (range.length != 2) {
                    throw new InvalidCronException(fieldType.getFieldName(), part, fieldType.getMin() + "-" + fieldType.getMax());
                }

                int start = parseNumber(range[0]);
                int end = parseNumber(range[1]);

                if (start > end || start < fieldType.getMin() || end > fieldType.getMax()) {
                    throw new InvalidCronException(fieldType.getFieldName(), part, fieldType.getMin() + "-" + fieldType.getMax());
                }
                for (int i = start; i <= end; i++) result.add(i);
            } else if (part.startsWith("*/")) {
                // Handle step expressions (e.g., "*/15")
                int step = parseNumber(part.substring(2));
                if (step <= 0 || step > fieldType.getMax()) {
                    throw new InvalidCronException(fieldType.getFieldName(), part, "Step must be between 1 and " + fieldType.getMax());
                }
                for (int i = fieldType.getMin(); i <= fieldType.getMax(); i += step) result.add(i);
            } else {
                // Handle single values (e.g., "5")
                int singleValue = parseNumber(part);
                if (singleValue < fieldType.getMin() || singleValue > fieldType.getMax()) {
                    throw new InvalidCronException(fieldType.getFieldName(), part, fieldType.getMin() + "-" + fieldType.getMax());
                }
                result.add(singleValue);
            }
        }
        return formatOutput(result);
    }

    private int parseNumber(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new InvalidCronException(fieldType.getFieldName(), str, fieldType.getMin() + "-" + fieldType.getMax());
        }
    }

    private String formatOutput(Set<Integer> values) {
        StringBuilder sb = new StringBuilder();
        for (int val : values) {
            sb.append(val).append(" ");
        }
        return sb.toString().trim();
    }

    public String getFormattedValues() {
        return values;
    }
}
