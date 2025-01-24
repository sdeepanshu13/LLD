package services;


import models.Profile;
import models.Report;

import java.util.*;

public class AdminServiceImpl implements interfaces.AdminService {

    private Map<String, Profile> profiles;

    public AdminServiceImpl(Map<String, Profile> profiles) {
        this.profiles = profiles;
    }

    public void buyBoost(String userId) {
        try{
            Profile profile = profiles.get(userId);
            if(profile == null) {
                throw new IllegalArgumentException("User not found");
            }
            profile.setBoosted(true);
            System.out.println("Boosted user: " + profile.getName());
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("User not found" + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Error in buying boost: " + e.getMessage());
        }
    }

    @Override
    public Report generateReport() {

        try {
            int totalUsers = profiles.size();
            long matchedUsers = profiles.values().stream().filter(profile -> !profile.getMatchedProfiles().isEmpty()).count();
            return new Report(totalUsers, (int) matchedUsers, new ArrayList<>(), Map.of());
        } catch (Exception e) {
            System.out.println("Error in generating report: " + e.getMessage());

            return null;
        }
    }
}

