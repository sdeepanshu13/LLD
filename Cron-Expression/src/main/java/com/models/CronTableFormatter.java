package com.models;

import com.enums.CronFieldType;
import com.Interfaces.TableFormatter;

import java.util.Map;

public class CronTableFormatter implements TableFormatter {
    @Override
    public String format(Map<CronFieldType, CronField> fields, String command) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<CronFieldType, CronField> entry : fields.entrySet()) {
            sb.append(String.format("%-14s %s%n", entry.getKey().toString().toLowerCase().replace("_", " "), entry.getValue().getFormattedValues()));
        }
        sb.append(String.format("%-14s %s%n", "command", command));
        return sb.toString();
    }
}
