package main;

/**
 * Enum representing the types of cron fields.
 */
public enum CronFieldType {
    MINUTE("minute", 0, 59),
    HOUR("hour", 0, 23),
    DAY_OF_MONTH("day of month", 1, 31),
    MONTH("month", 1, 12),
    DAY_OF_WEEK("day of week", 0, 6);

    private final String fieldName;
    private final int min;
    private final int max;

    CronFieldType(String fieldName, int min, int max) {
        this.fieldName = fieldName;
        this.min = min;
        this.max = max;
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
