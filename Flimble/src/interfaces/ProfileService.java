package interfaces;

import models.PartnerPreference;
import models.Profile;

import java.util.List;

public interface ProfileService {
    void createProfile(String userId, String name, int age, String gender);
    void addInterest(String userId, List<String> interests);
    void setPartnerPreference(String userId, PartnerPreference preference);
    Profile getBestProfile(String userId);
    void acceptProfile(String userId, String profileId);
    void declineProfile(String userId, String profileId);
    void removeProfile(String userId, String profileId);
    List<Profile> listMatchedProfiles(String userId);
    void superAcceptProfile(String userId, String profileId);
}
