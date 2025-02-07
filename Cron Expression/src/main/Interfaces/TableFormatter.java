package main.Interfaces;

import main.CronField;
import main.CronFieldType;

import java.util.Map;

/**
 * Interface for formatting cron output.
 */
public interface TableFormatter {
    String format(Map<CronFieldType, CronField> fields, String command);
}
