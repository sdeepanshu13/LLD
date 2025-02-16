package main;
import main.Interfaces.CronExpressionValidator;
import main.exception.InvalidCronException;

/**
 * Default implementation of cron expression validation.
 */
public class DefaultCronExpressionValidator implements CronExpressionValidator {
    @Override
    public void validate(String cronExpression) {
        if (cronExpression == null || cronExpression.trim().isEmpty()) {
            throw new InvalidCronException("Cron expression cannot be null or empty.");
        }

        String[] parts = cronExpression.trim().split("\\s+");
        if (parts.length != 6) {
            throw new InvalidCronException("Cron expression must contain 5 time fields + command." +
                    " Found: " + parts.length + " fields." + " in cron expression: " + cronExpression);
        }
    }
}
