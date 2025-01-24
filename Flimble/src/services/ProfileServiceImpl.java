package services;

import interfaces.ProfileService;
import models.PartnerPreference;
import models.Profile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// Implementation of the ProfileService interface
public class ProfileServiceImpl implements ProfileService {

    // A thread-safe map of user profiles, where the key is the userId, and the value is the Profile object
    private final Map<String, Profile> profiles = new ConcurrentHashMap<>();

    // A map to track declined profiles for each user, where the key is the userId, and the value is a set of declined userIds
    private final Map<String, Set<String>> declinedProfiles = new ConcurrentHashMap<>();

    // A map to track whether a user has used their one-time super-accept privilege
    private final Map<String, Boolean> superAcceptUsed = new ConcurrentHashMap<>();

    // Create a new profile for a user
    @Override
    public synchronized void createProfile(String userId, String name, int age, String gender) {
        try {
            // Add the new profile to the profiles map
            profiles.put(userId, new Profile(userId, name, age, gender, new ArrayList<>(), null));
            System.out.println("User created: " + profiles.get(userId).getName());

            // Initialize an empty set for declined profiles for this user
            declinedProfiles.put(userId, new HashSet<>());

            // Set the user's super-accept privilege to unused
            superAcceptUsed.put(userId, false);
        } catch (Exception e) {
            System.out.println("Error creating profile: " + e.getMessage());
        }
    }

    // Add interests to a user's profile
    @Override
    public synchronized void addInterest(String userId, List<String> interests) {
        try {
            // Retrieve the profile for the given userId
            Profile profile = profiles.get(userId);
            if (profile == null) throw new IllegalArgumentException("Profile not found");

            // Add the provided interests to the user's existing interests
            profile.getInterests().addAll(interests);
            System.out.println("Interests added for user: " + profile.getName() + " " + profile.getInterests());
        } catch (Exception e) {
            System.out.println("Error adding interests: " + e.getMessage());
        }
    }

    // Set partner preferences for a user
    @Override
    public synchronized void setPartnerPreference(String userId, PartnerPreference preference) {
        try {
            // Retrieve the profile for the given userId
            Profile profile = profiles.get(userId);
            if (profile == null) throw new IllegalArgumentException("Profile not found");

            // Set the partner preferences for the user
            profile.setPartnerPreference(preference);
            System.out.println("Partner preference set for user: " + profile.getName());
        } catch (Exception e) {
            System.out.println("Error setting partner preference: " + e.getMessage());
        }
    }

    // Get the best available profile for a user
    @Override
    public synchronized Profile getBestProfile(String userId) {
        try {
            // Retrieve the profile for the given userId
            Profile userProfile = profiles.get(userId);
            if (userProfile == null) throw new IllegalArgumentException("Profile not found");

            // Retrieve the user's partner preferences
            PartnerPreference preference = userProfile.getPartnerPreference();
            if (preference == null) throw new IllegalArgumentException("Partner preference not set");

            // Get the set of profiles the user has declined
            Set<String> declined = declinedProfiles.get(userId);

            // Filter and sort profiles to find the best match
            return profiles.values().stream()
                    .filter(p -> !p.getUserId().equals(userId)) // Exclude the user's own profile
                    .filter(p -> !declined.contains(p.getUserId())) // Exclude declined profiles
                    .filter(p -> !userProfile.getMatchedProfiles().contains(p.getUserId())) // Exclude already matched profiles
                    .filter(p -> isValidMatch(preference, p)) // Match profiles based on partner preferences
                    .sorted((p1, p2) -> {
                        // Calculate scores for ranking profiles
                        long score1 = calculateMatchScore(preference, p1) - getMatchPenalty(p1);
                        long score2 = calculateMatchScore(preference, p2) - getMatchPenalty(p2);

                        // Boosted profiles get higher scores
                        if (p1.isBoosted()) score1 += 50;
                        if (p2.isBoosted()) score2 += 50;

                        return Long.compare(score2, score1); // Higher score profiles come first
                    })
                    .findFirst()
                    .orElse(null); // Return the best profile or null if none found
        } catch (Exception e) {
            System.out.println("Error getting best profile: " + e.getMessage());
            return null;
        }
    }

    public String matchRequest(String userId, String profileId)
    {
        Scanner scanner = new Scanner(System.in);

        Profile userProfile = profiles.get(userId);
        Profile bestProfile = profiles.get(profileId);

         System.out.println("User : " + userId + " (" + userProfile.getName() + "): " +
                          " (Has Sent you Match Request Based on Mutual Interests To User: " +
                         bestProfile.getName()+ ")");

                // Accept or reject logic
                System.out.print("Do you want to ACCEPT or REJECT this profile? (Type 'y' or 'n'): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if ("y".equals(response)) {
                    return "y";
                } else if ("n".equals(response)) {
                   return "n";
                } else {
                    System.out.println("Invalid response. Profile skipped.\n");
                }


        return "";
    }



    // Accept a profile
    @Override
    public synchronized void acceptProfile(String userId, String profileId) {
        try {
            // Retrieve the user and the profile being accepted
            Profile userProfile = profiles.get(userId);
            Profile matchedProfile = profiles.get(profileId);

            if (userProfile == null || matchedProfile == null) throw new IllegalArgumentException("Profile not found");

            if(Objects.equals(matchRequest(userId, profileId), "y")) {

                // Add the accepted profile to the user's matched list
                userProfile.addMatchedProfile(profileId);

                matchedProfile.addMatchedProfile(userId);

                System.out.println("User " + userId + " (" + userProfile.getName() + ") accepted profile " + profileId + " (" + matchedProfile.getName() + ")");
            }
            else{
                declineProfile(userId,profileId);
            }
        } catch (Exception e) {
            System.out.println("Error accepting profile: " + e.getMessage());
        }
    }

    // Remove a matched profile
    @Override
    public synchronized void removeProfile(String userId, String profileId) {
        try {
            Profile userProfile = profiles.get(userId);
            Profile matchedProfile = profiles.get(profileId);

            if (userProfile == null || matchedProfile == null) throw new IllegalArgumentException("Profile not found");

            // Check if the profiles are matched
            if (!userProfile.getMatchedProfiles().contains(profileId) || !matchedProfile.getMatchedProfiles().contains(userId)) {
                throw new IllegalArgumentException("Profile not matched");
            }

            // Remove the profiles from each other's matched lists
            userProfile.removeMatchedProfile(profileId);
            matchedProfile.removeMatchedProfile(userId);

            // Add them to the declined profiles
            declinedProfiles.get(userId).add(profileId);
            declinedProfiles.get(profileId).add(userId);

            System.out.println(" ******  User " + userId + " (" + userProfile.getName() + ") removed profile " + profileId + " (" + matchedProfile.getName() + ") *****");
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error removing profile: " + e.getMessage());
        }
    }

    // Decline a profile
    @Override
    public synchronized void declineProfile(String userId, String profileId) {
        try {
            Profile userProfile = profiles.get(userId);
            Profile declinedProfile = profiles.get(profileId);
            if (userProfile == null) throw new IllegalArgumentException("Profile not found");

            // Add the profile to the declined list
            declinedProfiles.get(userId).add(profileId);
            declinedProfiles.get(profileId).add(userId);

            System.out.println("User " + userId + " (" + userProfile.getName() + ") declined profile " + profileId + " (" + declinedProfile.getName() + ")");
        } catch (Exception e) {
            System.out.println("Error declining profile: " + e.getMessage());
        }
    }

    // List all matched profiles for a user
    @Override
    public synchronized List<Profile> listMatchedProfiles(String userId) {
        try {
            Profile userProfile = profiles.get(userId);
            if (userProfile == null) throw new IllegalArgumentException("Profile not found");

            // Return a list of matched profiles
            return userProfile.getMatchedProfiles().stream()
                    .map(profiles::get)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Error listing matched profiles: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Allow a user to super-accept a profile
    @Override
    public synchronized void superAcceptProfile(String userId, String profileId) {
        try {
            if (Boolean.TRUE.equals(superAcceptUsed.get(userId))) {
                System.out.println("Super-accept already used by user: " + userId);
                return;
            }

            Profile targetProfile = profiles.get(profileId);
            Profile userProfile = profiles.get(userId);
            if (targetProfile == null) throw new IllegalArgumentException("Profile not found");

            // Mark super-accept as used and add the match
            superAcceptUsed.put(userId, true);
            targetProfile.addMatchedProfile(userId);

            System.out.println("User " + userId + " (" + userProfile.getName() + ") super-accepted profile " + profileId + " (" + targetProfile.getName() + ")");
        } catch (Exception e) {
            System.out.println("Error in super-accept: " + e.getMessage());
        }
    }

    // Helper method to validate if a profile matches the partner preferences
    private boolean isValidMatch(PartnerPreference preference, Profile profile) {
        if (preference.getGenderPreference() != null && !preference.getGenderPreference().equals(profile.getGender())) {
            return false;
        }

        if (profile.getAge() < preference.getMinAge() || profile.getAge() > preference.getMaxAge()) {
            return false;
        }

        if (!Collections.disjoint(preference.getInterests(), profile.getInterests())) {
            return true;
        }

        return preference.getPreferenceFlexibility().contains("interests");
    }

    // Calculate the match score for ranking profiles
    private long calculateMatchScore(PartnerPreference preference, Profile profile) {
        long score = 0;

        if (preference.getGenderPreference() != null && preference.getGenderPreference().equals(profile.getGender())) {
            score += 10;
        }

        if (profile.getAge() >= preference.getMinAge() && profile.getAge() <= preference.getMaxAge()) {
            score += 20;
        }

        long mutualInterests = profile.getInterests().stream()
                .filter(preference.getInterests()::contains)
                .count();

        score += mutualInterests * 5;

        return score;
    }

    // Apply a penalty to the match score based on the number of matches
    private long getMatchPenalty(Profile profile) {
        return profile.getMatchCount() * 10L;
    }

    // Get all profiles
    public synchronized Map<String, Profile> getProfiles() {
        return profiles;
    }

    // Get the name of a profile by userId
    public String getProfileName(String userId) {
        try {
            Profile profile = profiles.get(userId);
            if (profile == null) throw new IllegalArgumentException("Profile not found");
            return profile.getName();
        } catch (Exception e) {
            System.out.println("Error retrieving profile name: " + e.getMessage());
            return null;
        }
    }

    // Get the total number of profiles
    public int getTotalProfiles() {
        return profiles.size();
    }

    // Get the list of declined profiles for a user
    public synchronized List<Profile> getDeclinedProfiles(String userId) {
        try {
            Profile userProfile = profiles.get(userId);
            if (userProfile == null) throw new IllegalArgumentException("Profile not found");

            // Retrieve and map declined profiles to Profile objects

            return declinedProfiles.get(userId).stream()
                    .map(profiles::get)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.out.println("Error listing declined profiles: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public int calculateMutualInterests(Profile userProfile, Profile bestProfile) {
        return (int) userProfile.getInterests().stream()
                .filter(bestProfile.getInterests()::contains)
                .count();
    }

    public void printBoostedProfiles() {
        List<Profile> boostedProfiles = profiles.values().stream()
                .filter(Profile::isBoosted)
                .toList();

        System.out.println("Boosted profiles:");
        System.out.print("List of Boosted profile: ");
        for (Profile profile : boostedProfiles) {
            System.out.print("  " + profile.getName());
        }
        System.out.println();
    }

    public void printListMatchedProfiles() {
        for (int i = 1; i <= getTotalProfiles(); i++) {

            List<Profile> matchedProfiles = listMatchedProfiles(String.valueOf(i));
            System.out.print("Matched profiles for user " + getProfileName(String.valueOf(i)) + ": ");
            for (Profile profile : matchedProfiles) {
                System.out.print("   " + profile.getName());
            }
            System.out.println();
        }
    }

    public void printDeclinedProfiles() {
        for (int i = 1; i <= getTotalProfiles(); i++) {
            List<Profile> declinedProfiles = getDeclinedProfiles(String.valueOf(i));
             System.out.print("Declined profiles for user " + getProfileName(String.valueOf(i)) + ": ");
            for (Profile profile : declinedProfiles) {
                System.out.print("   " + profile.getName());
            }
            System.out.println();
        }
    }

    public void findBestMatches() {
        Scanner scanner = new Scanner(System.in);
        for (int i = 1; i <= getTotalProfiles(); i++) {
            String userId = String.valueOf(i);
            Profile bestProfile = getBestProfile(userId);

            if (bestProfile != null) {
                System.out.println("Best profile for user " + userId + " (" + getProfiles().get(userId).getName() + "): " +
                        bestProfile.getName() + " (Mutual Interests: " +
                        calculateMutualInterests(getProfiles().get(userId), bestProfile) + ")");

                // Accept or reject logic
                System.out.print("Do you want to ACCEPT or REJECT this profile? (Type 'y' or 'n'): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if ("y".equals(response)) {
                    acceptProfile(userId, bestProfile.getUserId());
                    System.out.println("Profile accepted.\n");
                    System.out.println();
                } else if ("n".equals(response)) {
                    declineProfile(userId, bestProfile.getUserId());
                    System.out.println("Profile rejected.\n");
                    System.out.println();
                } else {
                    System.out.println("Invalid response. Profile skipped.\n");
                }
            } else {
                System.out.println("No best profile found for user " + userId + ".\n");
            }
        }
    }


}

/*

*/
