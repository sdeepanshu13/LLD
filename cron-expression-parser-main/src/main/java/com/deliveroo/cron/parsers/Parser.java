package com.deliveroo.cron.parsers;

import com.deliveroo.cron.model.TimeField;

import java.util.List;

public abstract class Parser {
    public abstract String getRegex();
    public abstract List<Integer> getTimings(TimeField timeField, String cronExpression);
}
