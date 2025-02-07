package com.deliveroo.cron.parsers;

import com.deliveroo.cron.CronExpressionParser;
import com.deliveroo.cron.managers.IParsingManager;
import com.deliveroo.cron.managers.ParsingManager;
import com.deliveroo.cron.model.CronExpressionResponse;
import com.deliveroo.cron.model.TimeField;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    @Test
    public void cron_expression_parsing_positive() throws RuntimeException {
        IParsingManager parsingManager = new ParsingManager();
        parsingManager.registerParsers();

        CronExpressionParser cronExpressionParser = new CronExpressionParser(parsingManager);

        CronExpressionResponse cronExpressionResponse = cronExpressionParser.parseExpression("*/15 0 1,15 * 1-5 /usr/bin/find");
        assertNotNull(cronExpressionResponse);
    }

    @Test
    public void cron_expression_parsing_negative() throws RuntimeException {
        IParsingManager parsingManager = new ParsingManager();
        parsingManager.registerParsers();

        CronExpressionParser cronExpressionParser = new CronExpressionParser(parsingManager);
        try {
            cronExpressionParser.parseExpression("*/15 0 1,15 # 1-59 /usr/bin/find");
        }
        catch(RuntimeException re) {
            assertEquals("Invalid cron expression entered! Cannot be parsed!", re.getMessage());
        }
    }

    @Test
    public void star_parsing_positive() throws RuntimeException {
        Parser p = new StarParser();
        List<Integer> result;

        result = p.getTimings(TimeField.DAY_OF_WEEK,"*");
        assertEquals("[1, 2, 3, 4, 5, 6, 7]", result.toString());
    }

    @Test
    public void star_parsing_negative()  {
        Parser parser = new StarParser();
        expressionParserUtil("#", TimeField.DAY_OF_MONTH, "Invalid value entered!", parser);
    }

    @Test
    public void range_parsing_positive() throws RuntimeException {
        Parser p = new RangeParser();
        List<Integer> result;

        result = p.getTimings(TimeField.HOUR,"1-3");
        assertEquals("[1, 2, 3]", result.toString());

        result = p.getTimings(TimeField.DAY_OF_WEEK,"4-7");
        assertEquals("[4, 5, 6, 7]", result.toString());

        result = p.getTimings(TimeField.MINUTE,"2-2");
        assertEquals("[2]", result.toString());
    }

    @Test
    public void range_parsing_negative()  {
        Parser parser = new RangeParser();
        expressionParserUtil("2-57", TimeField.DAY_OF_MONTH, "Entered values are not in the range of Time Field", parser);
        expressionParserUtil("70-80", TimeField.MINUTE, "Entered values are not in the range of Time Field", parser);
        expressionParserUtil("2-10", TimeField.DAY_OF_WEEK, "Entered values are not in the range of Time Field", parser);
    }

    @Test
    public void number_parsing_positive() throws RuntimeException {
        Parser p = new NumberParser();
        List<Integer> result;

        result = p.getTimings(TimeField.HOUR,"1");
        assertEquals("[1]", result.toString());

        result = p.getTimings(TimeField.DAY_OF_WEEK,"4");
        assertEquals("[4]", result.toString());

        result = p.getTimings(TimeField.MINUTE,"25");
        assertEquals("[25]", result.toString());
    }

    @Test
    public void number_parsing_negative()  {
        Parser parser = new NumberParser();
        expressionParserUtil("-1", TimeField.DAY_OF_MONTH, "Incorrect time value provided!", parser);
        expressionParserUtil("60", TimeField.MINUTE, "Incorrect time value provided!", parser);
        expressionParserUtil("8", TimeField.DAY_OF_WEEK, "Incorrect time value provided!", parser);
    }

    @Test
    public void multiple_values_parsing_positive() throws RuntimeException {
        Parser p = new MultipleValuesParser();
        List<Integer> result;

        result = p.getTimings(TimeField.HOUR,"1,2,3");
        assertEquals("[1, 2, 3]", result.toString());

        result = p.getTimings(TimeField.HOUR,"1,1,1");
        assertEquals("[1]", result.toString());

        result = p.getTimings(TimeField.HOUR,"3,1,2");
        assertEquals("[1, 2, 3]", result.toString());
    }

    @Test
    public void multiple_values_parsing_negative()  {
        Parser parser = new MultipleValuesParser();
        expressionParserUtil("-1,35", TimeField.DAY_OF_MONTH, "Incorrect time value entered!", parser);
        expressionParserUtil("2,34", TimeField.DAY_OF_MONTH, "Incorrect time value entered!", parser);
        expressionParserUtil("0,5", TimeField.DAY_OF_WEEK, "Incorrect time value entered!", parser);
    }
    
    @Test
    public void increment_parsing_positive() throws RuntimeException {
        Parser p = new IncrementParser();
        List<Integer> result;

        result = p.getTimings(TimeField.HOUR,"*/10");
        assertEquals("[0, 10, 20]", result.toString());

        result = p.getTimings(TimeField.HOUR,"*/24");
        assertEquals("[0]", result.toString());

        result = p.getTimings(TimeField.DAY_OF_MONTH,"*/20");
        assertEquals("[1, 21]", result.toString());

        result = p.getTimings(TimeField.DAY_OF_WEEK,"*/2");
        assertEquals("[1, 3, 5, 7]", result.toString());
    }
    @Test
    public void increment_parsing_negative()  {
        Parser parser = new IncrementParser();
        expressionParserUtil("*/0", TimeField.DAY_OF_MONTH, "Invalid interval value entered!", parser);
        expressionParserUtil("*/10/10", TimeField.DAY_OF_MONTH, "Invalid interval value entered!", parser);
        expressionParserUtil("*/A", TimeField.DAY_OF_MONTH, "Invalid interval value entered!", parser);
    }

    private void expressionParserUtil(String incomingText, TimeField timeField, String msg, Parser parser) {
        try {
            parser.getTimings(timeField,incomingText);
        }
        catch(RuntimeException re) {
            assertEquals(msg, re.getMessage());
        }
    }
}