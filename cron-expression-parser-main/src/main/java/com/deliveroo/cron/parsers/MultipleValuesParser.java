package com.deliveroo.cron.parsers;

import com.deliveroo.cron.model.TimeField;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MultipleValuesParser extends Parser {

    private boolean isValid(int timeValue, TimeField timeField) {
        return timeValue >= timeField.getStartValue() && timeValue <= timeField.getEndValue();
    }
    @Override
    public List<Integer> getTimings(TimeField timeField, String cronExpression) {
        Set<Integer> result = new TreeSet<>();
        String[] cronValues = cronExpression.split(",");
        for(String cronValue: cronValues) {
            int timeValue = Integer.valueOf(cronValue);
            if(isValid(timeValue,timeField)) {
                result.add(timeValue);
            }
            else {
                throw new RuntimeException("Incorrect time value entered!");
            }
        }
        List<Integer> mainList = new ArrayList<>();
        mainList.addAll(result);
        return mainList;
    }

    @Override
    public String getRegex(){
        return "^\\d+(,\\d+)*$";
    }
}
