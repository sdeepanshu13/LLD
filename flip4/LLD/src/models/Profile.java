package models;

import java.util.*;

public class Profile {

    private String name;
    private int age;
    private String userId;
    private String gender;
    private List<String> interests;
    private PartnerPreference partnerPreference;
    private boolean boosted;
    private int matchCount;
    private List<String> matchedProfiles;


    public Profile(String userId, String name, int age, String gender, List<String> interests, PartnerPreference partnerPreference, boolean boosted, int matchCount, List<String> matchedProfiles) {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.interests = interests;
        this.partnerPreference = partnerPreference;
        this.boosted = boosted;
        this.matchCount = matchCount;
        this.matchedProfiles = matchedProfiles;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public PartnerPreference getPartnerPreference() {
        return partnerPreference;
    }

    public void setPartnerPreference(PartnerPreference partnerPreference) {
        this.partnerPreference = partnerPreference;
    }

    public boolean isBoosted() {
        return boosted;
    }

    public void setBoosted(boolean boosted) {
        this.boosted = boosted;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    public List<String> getMatchedProfiles() {
        return matchedProfiles;
    }

    public void setMatchedProfiles(List<String> matchedProfiles) {
        this.matchedProfiles = matchedProfiles;
    }

}
