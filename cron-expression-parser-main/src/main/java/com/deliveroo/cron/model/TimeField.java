package com.deliveroo.cron.model;

public enum TimeField {
    MINUTE("minute",0,59),
    HOUR("hour",0,23),
    DAY_OF_MONTH("day of month",1,31),
    MONTH("month",1,12),
    DAY_OF_WEEK("day of week",1,7);

    private String name;
    private int startValue;
    private int endValue;

    TimeField(String name, int startValue, int endValue) {
        this.name = name;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public String getName() {
        return name;
    }

    public int getStartValue() {
        return startValue;
    }

    public int getEndValue() {
        return endValue;
    }
}
