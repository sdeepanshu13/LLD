package com.deliveroo.cron.model;

import java.util.List;

import static java.lang.String.format;

public class CronExpressionResponse {
    private List<Integer> minute;
    private List<Integer> hour;
    private List<Integer> dayOfMonth;
    private List<Integer> month;
    private List<Integer> dayOfWeek;
    private String cronCommand;

    public CronExpressionResponse(List<Integer> minute, List<Integer> hour, List<Integer> dayOfMonth, List<Integer> month, List<Integer> dayOfWeek, String cronCommand) {
        this.minute = minute;
        this.hour = hour;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.dayOfWeek = dayOfWeek;
        this.cronCommand = cronCommand;
    }

    public void printResponse() {
        StringBuffer b = new StringBuffer();
        b.append(format("%-14s%s\n", TimeField.MINUTE.getName(), printList(minute)));
        b.append(format("%-14s%s\n", TimeField.HOUR.getName(), printList(hour)));
        b.append(format("%-14s%s\n", TimeField.DAY_OF_MONTH.getName(), printList(dayOfMonth)));
        b.append(format("%-14s%s\n", TimeField.MONTH.getName(), printList(month)));
        b.append(format("%-14s%s\n", TimeField.DAY_OF_WEEK.getName(), printList(dayOfWeek)));
        b.append(format("%-14s%s\n", "command", cronCommand));
        System.out.println(b.toString());
    }

    private String printList(List<Integer> cronValues) {
        String result = "";
        for(Integer cronValue: cronValues) {
            result += cronValue.toString() + " ";
        }
        return result;
    }
}
