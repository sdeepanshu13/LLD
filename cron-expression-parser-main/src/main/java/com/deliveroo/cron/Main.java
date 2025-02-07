package com.deliveroo.cron;

import com.deliveroo.cron.managers.IParsingManager;
import com.deliveroo.cron.managers.ParsingManager;
import com.deliveroo.cron.model.CronExpressionResponse;

public class Main {
    public static void main(String[] args) {
        if(args.length != 1 && args[0].split(" ").length != 6) {
            throw new RuntimeException("Invalid number of arguments passed!");
        }

        String cronExpression = args[0];

        IParsingManager parsingManager = new ParsingManager();
        parsingManager.registerParsers();

        CronExpressionParser cronExpressionParser = new CronExpressionParser(parsingManager);
        try{
            CronExpressionResponse cronExpressionResponse = cronExpressionParser.parseExpression(cronExpression);
            cronExpressionResponse.printResponse();
        } catch(RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
    }
}