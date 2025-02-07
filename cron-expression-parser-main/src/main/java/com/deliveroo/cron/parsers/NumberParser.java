package com.deliveroo.cron.parsers;

import com.deliveroo.cron.model.TimeField;

import java.util.ArrayList;
import java.util.List;

public class NumberParser extends Parser {
    @Override
    public List<Integer> getTimings(TimeField timeField, String cronExpression) {
        List<Integer> result = new ArrayList<>();
        int timeValue = Integer.valueOf(cronExpression);
        if(timeValue >= timeField.getStartValue() && timeValue <= timeField.getEndValue()) {
            result.add(timeValue);
        }
        else{
            throw new RuntimeException("Incorrect time value provided!");
        }
        return result;
    }

    @Override
    public String getRegex(){
        return "^\\d+$";
    }
}
