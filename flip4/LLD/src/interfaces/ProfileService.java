package interfaces;

import models.PartnerPreference;
import models.Profile;

import java.util.*;

public interface ProfileService {

    void createProfile(String userId, String name, int age, String gender);
    void addInterest(String userID, List<String> interests);
    void setPartnerPreference(String userID, PartnerPreference preference);
    Profile getBestProfile(String userID);
    void acceptProfile(String userID, String profileID);
    void declineProfile(String userID, String profileID);
    List<Profile> listMatchedProfiles(String userID);
}
