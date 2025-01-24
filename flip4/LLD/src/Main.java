import interfaces.AdminService;
import interfaces.ProfileService;
import models.PartnerPreference;
import models.Profile;
import models.Report;
import services.AdminServiceImpl;
import services.ProfileServiceImpl;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            ProfileServiceImpl profileService = new ProfileServiceImpl();
            AdminService adminService = new AdminServiceImpl(((ProfileServiceImpl) profileService).getProfiles());

            // Create profiles
            profileService.createProfile("1", "Alice", 30, "F");
            profileService.createProfile("2", "Bob", 30, "M");
            profileService.createProfile("3", "Charlie", 35, "M");
            profileService.createProfile("4", "David", 40, "M");
            profileService.createProfile("5", "Eve", 45, "F");
            profileService.createProfile("6", "Frank", 27, "M");
            profileService.createProfile("7", "Grace", 32, "F");
            profileService.createProfile("8", "Hannah", 24, "F");
            profileService.createProfile("9", "Ivan", 29, "M");
            profileService.createProfile("10", "Julia", 34, "F");

            // Add interests
            profileService.addInterest("1", List.of("Music", "Dance", "Reading", "Travelling"));
            profileService.addInterest("2", List.of("Books", "Travelling", "Reading", "Music"));
            profileService.addInterest("3", List.of("Hiking", "WineTasting", "Adventure","Reading"));
            profileService.addInterest("4", List.of("Concert", "Music", "MindReading","Dance"));
            profileService.addInterest("5", List.of("Music", "Travelling", "Books", "Dance"));
            profileService.addInterest("6", List.of("Monuments", "Dance", "Art","Reading"));
            profileService.addInterest("7", List.of("Hiking", "Beaches", "Reading", "Music"));
            profileService.addInterest("8", List.of("Music", "Mountains", "Reading", "Dance"));
            profileService.addInterest("9", List.of("Drinking", "Smoking", "Drugs", "Dance"));
            profileService.addInterest("10", List.of("Music", "Travelling", "Reading","Dance"));

            // Set partner preferences
            profileService.setPartnerPreference("1", new PartnerPreference(25, 45, "M", List.of("age"), List.of()));
            profileService.setPartnerPreference("2", new PartnerPreference(25, 40, "F", List.of( "gender", "interests"), List.of("Books", "Music")));
            profileService.setPartnerPreference("3", new PartnerPreference(30, 45, "F", List.of("gender"), List.of("Hiking")));
            profileService.setPartnerPreference("4", new PartnerPreference(35, 50, "F", List.of("age", "gender"), List.of("Dance")));
            profileService.setPartnerPreference("5", new PartnerPreference(30, 55, "M", List.of("age"), List.of( "Travelling")));
            profileService.setPartnerPreference("6", new PartnerPreference(20, 35, "F", List.of("gender"), List.of( "Reading")));
            profileService.setPartnerPreference("7", new PartnerPreference(20, 40, "M", List.of("gender","interests"), List.of( "Reading")));
            profileService.setPartnerPreference("8", new PartnerPreference(20, 35, "M", List.of("interest", "gender"), List.of("Reading")));
            profileService.setPartnerPreference("9", new PartnerPreference(20, 35, "M", List.of(), List.of("Hiking")));
            profileService.setPartnerPreference("10", new PartnerPreference(20, 35, "M", List.of("gender"), List.of("Music")));

            // Find and accept/decline profiles
            for(int i=1; i<=10; i++) {
                Profile bestProfile = profileService.getBestProfile(String.valueOf(i));
                if(i ==5 ||  i==7) {
                    profileService.rejectProfile(String.valueOf(i), bestProfile.getUserId());
                    System.out.println("Profile rejected for user " + profileService.getName(String.valueOf(i)));
                    continue;
                }
                System.out.println("Best match for user " + profileService.getName(String.valueOf(i)) +" is: "+ (bestProfile != null ? bestProfile.getName() : "No match found"));
                if (bestProfile != null) profileService.acceptProfile(String.valueOf(i), bestProfile.getUserId());
            }

            System.out.println();



            // List matched profiles for user1
            for(int i=1; i<=10; i++) {
                List<Profile> matchedProfilesUser = profileService.listMatchedProfiles(String.valueOf(i));
                System.out.print("Matched profiles for user " + profileService.getName(String.valueOf(i)) + ": ");
                for (Profile profile : matchedProfilesUser) {
                    System.out.print(profile.getName() + " | ");
                }
                System.out.println();
            }
            System.out.println();

            // Admin features
            adminService.buyBoost("3");

              adminService.buyBoost("5");


            // Decline a profile
            profileService.declineProfile("2", "1");


            for(int i=1; i<=10; i++) {
                List<Profile> matchedProfilesUser = profileService.listMatchedProfiles(String.valueOf(i));
                System.out.print("Matched profiles for user " + profileService.getName(String.valueOf(i)) + ": ");
                for (Profile profile : matchedProfilesUser) {
                    System.out.print(profile.getName() + " | ");
                }
                System.out.println();
            }

            // Generate and display report
            Report report = adminService.generateReport();
            System.out.println("Total users: " + report.getTotalUserCount());
            System.out.println("Matched users: " + report.getMatchedUserCount());

        } catch (Exception e) {
            System.out.println("Error in main: " + e.getMessage());
        }
    }
}
/*

You are launching the “Flimble” app to compete in the dating apps market. Design a console application to prototype the features of “Flimble”.

Functional requirements
P0 - Basic features
Users can create their profile on the platform. A user profile may include (but not limited to) basic information like name, age, gender etc. // done
Users can choose their interests from a list of interests provided by the platform. Interests may include pets, football, movies, books etc. // done
Users can choose their partner preferences, like age range, gender etc.// done
Users can request the best available profile** and choose to either accept or decline. Ranking of profiles shall be basis the following order (highest to lowest)
Preferred* profiles that have already accepted the user; ordered by highest number of mutual interests.
Preferred profiles ordered by highest number of mutual interests.
Unpreferred profiles that have already accepted the user; ordered by highest number of mutual interests

* Preferred profile is one that strictly matches the partner preference of the user.
** Users should not get any unpreferred profile that has not already accepted the user
Once the user accepts or declines a profile, it should not appear in the user feed again.
If two profiles have mutually accepted each other, the matched profile moves to the user’s  matched list. A user can view their matched list of profiles at any time.

Operations - create-profile, add-interests, set-partner-preference, get-best-profile, accept-profile, decline-profile, list-matched-profiles
P1 - Advanced features
To maximize the number of matched users, every time a user receives a match the likelihood of their profile appearing on another user's feed goes down. This essentially makes sure the users that don’t have matches, get prioritized.
Users can also buy boost plans which makes the user skip the queue and increase their likelihood of appearing in more user feeds.
Admins should be able to pull reports from the platform at any time. Reports may include stats like total user count, matched users count, top-n users with highest matches, user cohort size (gender, age etc).

Operations - buy-boost, show-stats
P2 - Bonus features
Users can mark a preference as strict vs lenient. If a profile fails a lenient preference, it may still get treated similar to a preferred profile but with lower ranking. E.g. preference of strict:gender:male & lenient:age:22-25, could mark a male:21 profile as preferred profile, but ranks them lower than a male:23.
Users can super-accept a profile, which will immediately rank the user highest on the receivers’ feed. Users can only super-accept once in their lifetime.

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