package com.Interfaces;



import com.enums.CronFieldType;
import com.models.CronField;

import java.util.Map;

/**
 * Interface for formatting cron output.
 */
public interface TableFormatter {
    String format(Map<CronFieldType, CronField> fields, String command);
}
