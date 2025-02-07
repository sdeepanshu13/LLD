package com.deliveroo.cron;

import com.deliveroo.cron.managers.IParsingManager;
import com.deliveroo.cron.model.CronExpressionResponse;
import com.deliveroo.cron.model.TimeField;

import java.util.List;

public class CronExpressionParser {
    private IParsingManager parsingManager;

    public CronExpressionParser(IParsingManager parsingManager) {
        this.parsingManager = parsingManager;
    }

    public CronExpressionResponse parseExpression(String cronExpression) {
        String[] subCronExpressions = cronExpression.split(" ");

        List<Integer> MinuteResult = parsingManager.getTimingsList(TimeField.MINUTE,subCronExpressions[0]);
        List<Integer> HourResult = parsingManager.getTimingsList(TimeField.HOUR,subCronExpressions[1]);
        List<Integer> DayOfMonthResult = parsingManager.getTimingsList(TimeField.DAY_OF_MONTH,subCronExpressions[2]);
        List<Integer> MonthResult = parsingManager.getTimingsList(TimeField.MONTH,subCronExpressions[3]);
        List<Integer> DayOfWeekesult = parsingManager.getTimingsList(TimeField.DAY_OF_WEEK,subCronExpressions[4]);
        String cronCommand = subCronExpressions[5];

        CronExpressionResponse cronExpressionResponse = new CronExpressionResponse(MinuteResult,HourResult,DayOfMonthResult,MonthResult,DayOfWeekesult,cronCommand);
        return cronExpressionResponse;
    }
}
