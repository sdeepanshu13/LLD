package main;
import main.Interfaces.*;

public class Main {
    public static void main(String[] args) {
        System.out.println(" Running Cron Expression Parser...\n");

        //  Check if user provided input
        if (args.length == 0) {
            System.err.println(" Error: No cron expression provided.\nUsage: java -cp out main.Main \"<cron_expression>\"");
            System.exit(1);
        }

        //  Read user-provided cron expression
        String cronExpression = args[0].trim();

        //  Process user input
        System.out.println("\n Testing: " + cronExpression);
        System.out.flush(); // Ensure this prints before validation begins

        try {
            CronParser parser = new CronParser(
                    cronExpression,
                    new DefaultCronExpressionValidator(),
                    new CronTableFormatter()
            );

            String result = parser.parse();
            System.out.println("\n Parsed Output:\n" + result);
            System.out.flush(); // Ensure immediate output
        } catch (Exception e) {
            System.err.println("\n Error: Invalid Cron Expression -> " + cronExpression);
            System.err.println("    Reason: " + e.getMessage());
            System.err.flush(); // Ensure errors print immediately
        }
    }
}



/*
public class Main {
    public static void main(String[] args) {
        System.out.println(" Running Cron Expression Parser...\n");
        // Example Cron Expressions (Valid and Invalid)
        String[] cronExpressions = {
        */

          // "*/15 0 1,32 * 1-5 /usr/bin/find",  //  Invalid: Day of month 32 (should be 1-31)
           // "*/20 5 1,20 * 1-5 /usr/bin/find",  //  Valid
           // "60 0 1,15 * 1-5 /usr/bin/find",    //  Invalid: Minute 60 (should be 0-59)
          //  "*/10 25 1,15 * 1-5 /usr/bin/find", //  Invalid: Hour 25 (should be 0-23)
          //  "*/5 12 1-5 0-15 1-5 /usr/bin/find", //  Invalid: Month 0-15 (should be 1-12)
         //   "*/5 12 1-5 * 10 /usr/bin/find",    //  Invalid: Day of week 10 (should be 0-6)
        //    "*/5 * * * * /usr/bin/find",        //  Valid (runs every 5 minutes)
            // Test Cases for Command Validation
       //     "*/5 * * * * ",                      //  Invalid: Command is missing
      //      "*/5 * * * * 123invalid?",           //  Invalid: Command contains special characters
      //      "*/5 * * * * /bin/bash -c 'echo ok'", //  Valid: Multi-word command
        //    "*/5 * * * * ls -la /home/user"       //  Valid: Command with arguments
     //   };
/*
        // Test each cron expression
        for (String cronExpression : cronExpressions) {
            System.out.println(" Testing: " + cronExpression);
            try {
                CronParser parser = new CronParser(
                        cronExpression,
                        new DefaultCronExpressionValidator(),
                        new CronTableFormatter()
                );
                String result = parser.parse();
                System.out.println(" Parsed Output:\n" + result);
            } catch (Exception e) {
                System.out.println(" Error: Invalid Cron Expression -> " + cronExpression);
                System.out.println(" Reason: " + e.getMessage());
            }
            System.out.println();
        }
        System.out.println("\n Running Tests...");
        TestRunner.runTests();
    }
}
*/



