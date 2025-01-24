package services;

import interfaces.ProfileService;
import models.PartnerPreference;
import models.Profile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProfileServiceImpl implements ProfileService {

    private Map<String, Profile> profiles = new ConcurrentHashMap<>();

    @Override
    public void createProfile(String userId, String name, int age, String gender) {
        try {
            Profile profile = new Profile(userId, name, age, gender, new ArrayList<>(), null, false, 0, new ArrayList<>());
            profiles.put(userId, profile);
        } catch (Exception e) {
            System.out.println("Error in creating profile: " + e.getMessage());
        }
    }

    @Override
    public void addInterest(String userID, List<String> interests) {
        try {
            Profile profile = profiles.get(userID);
            if (profile == null) throw new IllegalArgumentException("Profile not found");
            profile.getInterests().addAll(interests);
        } catch (Exception e) {
            System.out.println("Error in adding interest: " + e.getMessage());
        }
    }

    @Override
    public void setPartnerPreference(String userID, PartnerPreference preference) {
        try {
            Profile profile = profiles.get(userID);
            if (profile == null) throw new IllegalArgumentException("Profile not found");
            profile.setPartnerPreference(preference);
        } catch (Exception e) {
            System.out.println("Error in setting partner preference: " + e.getMessage());
        }
    }

    @Override
    public Profile getBestProfile(String userID) {
        try {
            Profile userProfile = profiles.get(userID);
            if (userProfile == null) throw new IllegalArgumentException("Profile not found");

            PartnerPreference userPreference = userProfile.getPartnerPreference();
            if (userPreference == null) throw new IllegalArgumentException("Partner preference not set");

            return profiles.values().stream()
                    .filter(profile -> !profile.getUserId().equals(userID)) // Exclude current user
                    .filter(profile -> isValidMatch(userPreference, profile)) // Filter based on preferences
                    .sorted(Comparator.comparingInt(profile -> Math.toIntExact(calculateMatchScore(userPreference, profile)))) // Sort by match score
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            System.out.println("Error in getting best profile: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void acceptProfile(String userID, String profileID) {
        try {
            Profile userProfile = profiles.get(userID);
            Profile matchedProfile = profiles.get(profileID);
            if (userProfile == null || matchedProfile == null) throw new IllegalAccessException("Profile not found");
            if(!userProfile.getMatchedProfiles().contains(profileID)) {

                userProfile.getMatchedProfiles().add(profileID);
                matchedProfile.getMatchedProfiles().add(userID);
            }
        } catch (Exception e) {
            System.out.println("Error in accepting profile: " + e.getMessage());
        }
    }

    public void rejectProfile(String userID, String profileID) {
        try {
            Profile userProfile = profiles.get(userID);
            Profile matchedProfile = profiles.get(profileID);
            if (userProfile == null || matchedProfile == null) throw new IllegalAccessException("Profile not found");

            userProfile.getMatchedProfiles().remove(profileID);

            matchedProfile.getMatchedProfiles().remove(userID);

            System.out.println("Profile: " + matchedProfile.getName() + " has been rejected successfully by : " + userProfile.getName());
        } catch (Exception e) {
            System.out.println("Error in accepting profile: " + e.getMessage());
        }
    }

    @Override
    public void declineProfile(String userID, String profileID) {
        try {
            Profile userProfile = profiles.get(userID);
            Profile matchedProfile = profiles.get(profileID);

            if (userProfile == null || matchedProfile == null) {
                throw new IllegalArgumentException("Profile not found");
            }
            if (!userProfile.getMatchedProfiles().contains(profileID)) {
                throw new IllegalStateException("Profile is not in the matched list for user");
            }

            userProfile.getMatchedProfiles().remove(profileID);
            matchedProfile.getMatchedProfiles().remove(userID);

            System.out.println("Profile: " + matchedProfile.getName() + " has been declined successfully by : " + userProfile.getName());
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error in declining profile: " + e.getMessage());
        }
    }

    @Override
    public List<Profile> listMatchedProfiles(String userID) {
        try {
            Profile profile = profiles.get(userID);
            if (profile == null) throw new IllegalArgumentException("Profile not found");
            return profile.getMatchedProfiles().stream().map(profiles::get).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Error in listing matched profiles: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private boolean isValidMatch(PartnerPreference preference, Profile profile) {
        // Match based on gender
        if (preference.getGenderPreference() != null && !preference.getGenderPreference().equals(profile.getGender())) {
            return false;
        }

        // Match based on age range
        if (profile.getAge() < preference.getMinAge() || profile.getAge() > preference.getMaxAge()) {
            return false;
        }

        // Match based on interests
        if (preference.getInterests() != null && !preference.getInterests().isEmpty()) {
            List<String> userInterests = profile.getInterests();
            // Check if any of the user's interests match the partner's preference
            if (userInterests.stream().noneMatch(interest -> preference.getInterests().contains(interest))) {
                return false;
            }
        }

        return true;
    }

    private long calculateMatchScore(PartnerPreference preference, Profile profile) {
        long score = 0;

        // Increase score if gender matches
        if (preference.getGenderPreference() != null && preference.getGenderPreference().equals(profile.getGender())) {
            score += 10;
        }

        // Increase score based on age preference
        if (profile.getAge() >= preference.getMinAge() && profile.getAge() <= preference.getMaxAge()) {
            score += 20;
        }

        // Increase score based on interests overlap
        if (preference.getInterests() != null && !preference.getInterests().isEmpty()) {
            long matchingInterests = profile.getInterests().stream()
                    .filter(preference.getInterests()::contains)
                    .count();
            score += matchingInterests * 5; // Each matching interest gives 5 points
        }

        return score;
    }

    public Map<String, Profile> getProfiles() {
        return profiles;
    }

    public String getName(String userId) {
        return profiles.get(userId).getName();
    }
}
