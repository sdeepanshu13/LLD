package com.deliveroo.cron.parsers;

import com.deliveroo.cron.model.TimeField;

import java.util.ArrayList;
import java.util.List;

public class StarParser extends Parser {
    @Override
    public List<Integer> getTimings(TimeField timeField, String cronExpression){
        List<Integer> result = new ArrayList<>();
        int startValue = timeField.getStartValue();
        int endValue = timeField.getEndValue();

        while(startValue <= endValue) {
            result.add(startValue);
            startValue++;
        }
        return result;
    }

    @Override
    public String getRegex(){
        return "^\\*$";
    }
}
