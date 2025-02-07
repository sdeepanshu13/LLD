package com.deliveroo.cron.managers;

import com.deliveroo.cron.model.TimeField;

import java.util.List;

public interface IParsingManager {
    List<Integer> getTimingsList(TimeField timeField, String cronExpression);
    void registerParsers();
}
