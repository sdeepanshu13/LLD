package com.deliveroo.cron.managers;

import com.deliveroo.cron.model.TimeField;
import com.deliveroo.cron.parsers.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ParsingManager implements IParsingManager {
    private Set<Parser> parserSet = new HashSet<>();

    @Override
    public List<Integer> getTimingsList(TimeField timeField, String cronExpression) {
        for(Parser parser: parserSet) {
            if(Pattern.matches(parser.getRegex(),cronExpression)) {
                return parser.getTimings(timeField,cronExpression);
            }
        }
        throw new RuntimeException("Invalid cron expression entered! Cannot be parsed!");
    }

    @Override
    public void registerParsers() {
        parserSet.add(new StarParser());
        parserSet.add(new IncrementParser());
        parserSet.add(new MultipleValuesParser());
        parserSet.add(new NumberParser());
        parserSet.add(new RangeParser());
    }
}
