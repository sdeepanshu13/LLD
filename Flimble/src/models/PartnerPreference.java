package models;

import java.util.List;

public class PartnerPreference {

    private int minAge;
    private int maxAge;
    private String genderPreference;
    private List<String> preferenceFlexibility;
    private List<String> interests;  // Added interests field

    // Constructor with interests
    public PartnerPreference(int minAge, int maxAge, String genderPreference, List<String> preferenceFlexibility, List<String> interests) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.genderPreference = genderPreference;
        this.preferenceFlexibility = preferenceFlexibility;
        this.interests = interests;  // Initialize interests
    }

    // Getters and setters
    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getGenderPreference() {
        return genderPreference;
    }

    public void setGenderPreference(String genderPreference) {
        this.genderPreference = genderPreference;
    }

    public List<String> getPreferenceFlexibility() {
        return preferenceFlexibility;
    }

    public void setPreferenceFlexibility(List<String> preferenceFlexibility) {
        this.preferenceFlexibility = preferenceFlexibility;
    }

    public List<String> getInterests() {
        return interests;  // Getter for interests
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;  // Setter for interests
    }
}
