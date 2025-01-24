package models;

import java.util.ArrayList;
import java.util.List;

public class Profile {

    private final String userId;
    private final String name;
    private final int age;
    private final String gender;
    private final List<String> interests;
    private PartnerPreference partnerPreference;
    private boolean boosted;
    private boolean superAcceptUsed;
    private int matchCount;
    private final List<String> matchedProfiles;


    public Profile(String userId, String name, int age, String gender, List<String> interests, PartnerPreference partnerPreference) {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.interests = interests != null ? interests : new ArrayList<>();
        this.partnerPreference = partnerPreference;
        this.boosted = false;
        this.superAcceptUsed = false;
        this.matchCount = 0;
        this.matchedProfiles = new ArrayList<>();
    }

    public synchronized String getUserId() {
        return userId;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized int getAge() {
        return age;
    }

    public synchronized String getGender() {
        return gender;
    }

    public synchronized List<String> getInterests() {
        return interests;
    }

    public synchronized void addInterest(String interest) {
        interests.add(interest);
    }

    public synchronized PartnerPreference getPartnerPreference() {
    System.out.println("Partner Preference Details:");
    if (partnerPreference != null) {
        System.out.println("Partner Preference Details for user: "+name);
        System.out.print("Min Age: " + partnerPreference.getMinAge());
        System.out.print(" || Max Age: " + partnerPreference.getMaxAge());
        System.out.print(" || Gender Preference: " + partnerPreference.getGenderPreference());
        System.out.print(" || Preference Flexibility: " + partnerPreference.getPreferenceFlexibility());
        System.out.print(" || Interests: " + partnerPreference.getInterests());
        System.out.println();
    } else {
        System.out.println("No preferences set.");
    }
    return partnerPreference;
}


    public synchronized void setPartnerPreference(PartnerPreference partnerPreference) {
        this.partnerPreference = partnerPreference;
    }

    public synchronized boolean isBoosted() {
        return boosted;
    }

    public synchronized void setBoosted(boolean boosted) {
        this.boosted = boosted;
    }

    public synchronized boolean isSuperAcceptUsed() {
        return superAcceptUsed;
    }

    public synchronized void useSuperAccept() {
        this.superAcceptUsed = true;
    }

    public synchronized int getMatchCount() {
        return matchCount;
    }

    public synchronized void incrementMatchCount() {
        this.matchCount++;
    }

    public synchronized List<String> getMatchedProfiles() {
        return matchedProfiles;
    }
     public synchronized void addMatchedProfile(String profileId) {
        if (!matchedProfiles.contains(profileId)) {
            matchedProfiles.add(profileId);
            incrementMatchCount();
        }
    }

    public synchronized void removeMatchedProfile(String profileId) {
        if (matchedProfiles.contains(profileId)) {
            matchedProfiles.remove(profileId);
            matchCount--;
        }
    }

}
