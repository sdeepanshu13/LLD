package com.deliveroo.cron.parsers;

import com.deliveroo.cron.model.TimeField;

import java.util.ArrayList;
import java.util.List;

public class RangeParser extends Parser {
    private boolean isValidRange(int startValue, int endValue, TimeField timeField) {
        return startValue >= timeField.getStartValue() && startValue <= endValue && endValue <= timeField.getEndValue();
    }
    @Override
    public List<Integer> getTimings(TimeField timeField, String cronExpression) {
        List<Integer> result = new ArrayList<>();
        String[] rangeValues = cronExpression.split("-");
        int startValue = Integer.valueOf(rangeValues[0]);
        int endValue = Integer.valueOf(rangeValues[1]);
        if(isValidRange(startValue,endValue,timeField)) {
            while(startValue <= endValue) {
                result.add(startValue);
                startValue++;
            }
        }
        else{
            throw new RuntimeException("Entered values are not in the range of Time Field");
        }
        return result;
    }

    @Override
    public String getRegex(){
        return "^\\d+\\-\\d+$";
    }
}
