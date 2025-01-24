    import models.PartnerPreference;
import models.Profile;
import models.Report;
import services.AdminServiceImpl;
import services.ProfileServiceImpl;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProfileServiceImpl profileService = new ProfileServiceImpl();

        AdminServiceImpl adminService = new AdminServiceImpl(profileService.getProfiles());

        // Test Case 1: Create profiles
        System.out.println("Creating profiles...");
        profileService.createProfile("1", "Alice", 30, "F");
        profileService.createProfile("2", "Bob", 32, "M");
        profileService.createProfile("3", "Charlie", 29, "M");
        profileService.createProfile("4", "Diana", 35, "F");
        profileService.createProfile("5", "Eve", 40, "F");
        profileService.createProfile("6", "Frank", 28, "M");
        profileService.createProfile("7", "Grace", 33, "F");
        profileService.createProfile("8", "Henry", 31, "M");
        profileService.createProfile("9", "Ivy", 34, "F");
        profileService.createProfile("10", "Jack", 27, "M");
        profileService.createProfile("11", "Kate", 26, "F");
        profileService.createProfile("12", "Leo", 29, "M");
        profileService.createProfile("13", "Mia", 30, "F");
        profileService.createProfile("14", "Nick", 31, "M");
        profileService.createProfile("15", "Olivia", 32, "F");


        System.out.println("Profiles created.\n");

        // Test Case 2: Boost some profiles initially
        System.out.println("Applying boosts...");
        adminService.buyBoost("2"); // Bob is boosted
        adminService.buyBoost("6"); // Frank is boosted
        System.out.println("Boosts applied.\n");

        // Test Case 3: Add interests to profiles
        System.out.println("Adding interests...");
        profileService.addInterest("1", List.of("Movies", "Music", "Books"));
        profileService.addInterest("2", List.of("Sports", "Music", "Travel"));
        profileService.addInterest("3", List.of("Books", "Hiking", "Cooking"));
        profileService.addInterest("4", List.of("Movies", "Cooking", "Travel"));
        profileService.addInterest("5", List.of("Art", "Music", "Travel"));
        profileService.addInterest("6", List.of("Books", "Music", "Travel"));
        profileService.addInterest("7", List.of("Movies", "Hiking", "Travel"));
        profileService.addInterest("8", List.of("Movies", "Books", "Travel"));
        profileService.addInterest("9", List.of("Hiking", "Music", "Travel"));
        profileService.addInterest("10", List.of("Movies", "Music", "Travel"));
        profileService.addInterest("11", List.of("Books", "Music", "Hiking"));
        profileService.addInterest("12", List.of("Movies", "Music", "Travel"));
        profileService.addInterest("13", List.of("Hiking", "Music", "Books"));
        profileService.addInterest("14", List.of("Movies", "Music", "Travel"));
        profileService.addInterest("15", List.of("Books", "Hiking", "Travel"));

        System.out.println("Interests added.\n");

        // Test Case 4: Set partner preferences
        System.out.println("Setting partner preferences...");
        profileService.setPartnerPreference("1", new PartnerPreference(25, 35, "M", List.of("age", "interests"), List.of("Music", "Books")));
        profileService.setPartnerPreference("2", new PartnerPreference(28, 35, "F", List.of("age", "gender"), List.of("Travel")));
        profileService.setPartnerPreference("3", new PartnerPreference(25, 40, "F", List.of("age"), List.of("Hiking", "Cooking")));
        profileService.setPartnerPreference("4", new PartnerPreference(28, 40, "M", List.of("age"), List.of("Movies","Hiking")));
        profileService.setPartnerPreference("5", new PartnerPreference(25, 40, "M", List.of("interests"), List.of("Music")));
        profileService.setPartnerPreference("6", new PartnerPreference(25, 35, "F", List.of("age", "interests"), List.of("Books")));
        profileService.setPartnerPreference("7", new PartnerPreference(25, 35, "M", List.of("age", "interests"), List.of("Music","Hiking")));
        profileService.setPartnerPreference("8", new PartnerPreference(25, 35, "F", List.of("age", "interests"), List.of("Music","Books")));
        profileService.setPartnerPreference("9", new PartnerPreference(25, 35, "M", List.of("age", "interests"), List.of("Music")));
        System.out.println("Partner preferences set.\n");
        System.out.println();

        // Test Case 5: Find best matches for all users with accept/reject options
        System.out.println("Finding best matches...");
        profileService.findBestMatches();

        System.out.println();


        // Test Case 6: List matched profiles for all users
        System.out.println("Listing matched profiles...");
        profileService.printListMatchedProfiles();

        System.out.println();

        profileService.removeProfile("1", "6");
        profileService.removeProfile("2", "15");

        System.out.println();



        System.out.println("Listing matched profiles...");
        profileService.printListMatchedProfiles();
        System.out.println();

        System.out.println("Listing Declined or removed profiles...");
       profileService.printDeclinedProfiles();
        System.out.println();


        System.out.println("Finding best matches...");
        profileService.findBestMatches();



         System.out.println();

        // Test Case 7: Generate admin report
        System.out.println("Generating admin report...");


        Report report = adminService.generateReport();
        if (report != null) {
            System.out.println("Total users: " + report.getTotalUserCount());
            System.out.println("Matched users: " + report.getMatchedUserCount());
            System.out.println("Top users by matches: " + report.getTopUsers());
            System.out.println("User cohorts: " + report.getUserCohorts());
        } else {
            System.out.println("Report generation failed.");
        }
        System.out.println();

        // Test Case 8: Edge case - Super-accept
        System.out.println("Using super-accept...");
        profileService.superAcceptProfile("6", "1"); // Frank super-accepts Alice
        System.out.println();

        profileService.printBoostedProfiles();


        System.out.println("All test cases executed.");
    }


}

/*

You are launching the “Flimble” app to compete in the dating apps market. Design a console application to prototype the features of “Flimble”.

Functional requirements
P0 - Basic features
Users can create their profile on the platform. A user profile may include (but not limited to) basic information like name, age, gender etc. // done
Users can choose their interests from a list of interests provided by the platform. Interests may include pets, football, movies, books etc. // done
Users can choose their partner preferences, like age range, gender etc.// done
Users can request the best available profile** and choose to either accept or decline. Ranking of profiles shall be basis the following order (highest to lowest)//done
Preferred* profiles that have already accepted the user; ordered by highest number of mutual interests.//done
Preferred profiles ordered by highest number of mutual interests.//done
Unpreferred profiles that have already accepted the user; ordered by highest number of mutual interests//done

* Preferred profile is one that strictly matches the partner preference of the user.//done
** Users should not get any unpreferred profile that has not already accepted the user
Once the user accepts or declines a profile, it should not appear in the user feed again.//done
If two profiles have mutually accepted each other, the matched profile moves to the user’s  matched list. A user can view their matched list of profiles at any time.//done

Operations - create-profile, add-interests, set-partner-preference, get-best-profile, accept-profile, decline-profile, list-matched-profiles
P1 - Advanced features
To maximize the number of matched users, every time a user receives a match the likelihood of their profile appearing on another user's feed goes down. This essentially makes sure the users that don’t have matches, get prioritized.//done
Users can also buy boost plans which makes the user skip the queue and increase their likelihood of appearing in more user feeds.//done
Admins should be able to pull reports from the platform at any time. Reports may include stats like total user count, matched users count, top-n users with highest matches, user cohort size (gender, age etc).//done

Operations - buy-boost, show-stats
P2 - Bonus features
Users can mark a preference as strict vs lenient. If a profile fails a lenient preference, it may still get treated similar to a preferred profile but with lower ranking. E.g. preference of strict:gender:male & lenient:age:22-25, could mark a male:21 profile as preferred profile, but ranks them lower than a male:23.
Users can super-accept a profile, which will immediately rank the user highest on the receivers’ feed. Users can only super-accept once in their lifetime.//done

Operations - super-accept-profile

Expectations and ground rules
Code should be demo’able (very important), either by using a main driver program or reading/writing on console(i.e. STDIN/STDOUT).
Create the sample data yourself. Add minimum 5-6 profiles to test your application. You can put it into a file, test case or main driver program itself.
Avoid writing monolithic or all-in-one file code. Code should be readable, modular, testable, extensible. Follow proper naming conventions of your programming language. It should be easy to add/remove functionality without rewriting the entire codebase.
Code should handle edge cases properly and fail gracefully.
Don’t use any database, store all the data in memory.
You are free to use any popular programming language of your choice.
Usage of any AI powered tools such a chatGPT or github-copilot is strictly prohibited. You may use the internet to look up any syntactic references.
The p2 bonus features are optional. Attempt them only after finishing the p0 & p1 features.
The problem may require you to make certain assumptions. Do make reasonable assumptions and convey them to the review panel.



 */