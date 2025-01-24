package services;


import interfaces.AdminService;
import models.Profile;
import models.Report;

import java.util.*;
import java.util.stream.Collectors;

// Implementation of the AdminService interface
public class AdminServiceImpl implements AdminService {

    // A map of user profiles, where the key is the userId, and the value is the Profile object
    private final Map<String, Profile> profiles;

    // Constructor to initialize the AdminServiceImpl with the existing profiles
    public AdminServiceImpl(Map<String, Profile> profiles) {
        this.profiles = profiles;
    }

    @Override
    public synchronized void buyBoost(String userId) {
        try {
            // Retrieve the profile for the given userId
            Profile profile = profiles.get(userId);
            if (profile == null) {
                // If the profile doesn't exist, throw an exception
                throw new IllegalArgumentException("User not found: " + userId);
            }
            // Set the profile as boosted
            profile.setBoosted(true);
            System.out.println("User " + profile.getName() + " has been boosted.");
        } catch (Exception e) {
            // Handle any errors during boosting
            System.out.println("Error in boosting user: " + e.getMessage());
        }
    }

    @Override
    public synchronized Report generateReport() {
        try {
            // Calculate the total number of users
            int totalUsers = profiles.size();

            // Count the number of users who have at least one match
            long matchedUsers = profiles.values().stream()
                    .filter(profile -> !profile.getMatchedProfiles().isEmpty())
                    .count();

            // Find the top 5 users with the highest number of matches
            List<String> topUsers = profiles.values().stream()
                    .sorted((p1, p2) -> Integer.compare(p2.getMatchCount(), p1.getMatchCount()))
                    .limit(5) // Take only the top 5
                    .map(Profile::getName) // Extract their names
                    .collect(Collectors.toList());

            // Group users into cohorts by gender and age range (e.g., "M-20s", "F-30s")
            Map<String, Integer> userCohorts = profiles.values().stream()
                    .collect(Collectors.groupingBy(
                            // Key: gender and decade of age
                            profile -> profile.getGender() + "-" + (profile.getAge() / 10 * 10) + "s",
                            // Value: count of users in each cohort
                            Collectors.summingInt(p -> 1)
                    ));

            // Return a new Report object containing all the calculated data
            return new Report(totalUsers, (int) matchedUsers, topUsers, userCohorts);
        } catch (Exception e) {
            // Handle errors during report generation
            System.out.println("Error generating report: " + e.getMessage());
            return null;
        }
    }
}
