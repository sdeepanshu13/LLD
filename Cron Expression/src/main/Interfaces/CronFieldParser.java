package main.Interfaces;

/**
 * Interface for parsing cron fields.
 */
public interface CronFieldParser {
    String parseField(String fieldExpression);
}
