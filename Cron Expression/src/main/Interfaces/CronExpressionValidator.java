package main.Interfaces;

/**
 * Interface for validating cron expressions.
 */
public interface CronExpressionValidator {
    void validate(String cronExpression);
}
