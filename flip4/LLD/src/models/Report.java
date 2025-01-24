package models;

import java.util.*;

public class Report {

    private int totalUserCount;
    private int matchedUserCount;
    private List<String> topUsers;
    private Map<String,Integer> userCohorts;

    public Report(int totalUserCount, int matchedUserCount, List<String> topUsers, Map<String,Integer> userCohorts) {
        this.totalUserCount = totalUserCount;
        this.matchedUserCount = matchedUserCount;
        this.topUsers = topUsers;
        this.userCohorts = userCohorts;
    }

    public int getTotalUserCount() {
        return totalUserCount;
    }
    public void setTotalUserCount(int totalUserCount) {
        this.totalUserCount = totalUserCount;
    }
    public int getMatchedUserCount() {
        return matchedUserCount;
    }
    public void setMatchedUserCount(int matchedUserCount) {
        this.matchedUserCount = matchedUserCount;
    }
    public List<String> getTopUsers() {
        return topUsers;
    }
    public void setTopUsers(List<String> topUsers) {
        this.topUsers = topUsers;
    }

    public Map<String,Integer> getUserCohorts() {
        return userCohorts;
    }
    public void setUserCohorts(Map<String,Integer> userCohorts) {
        this.userCohorts = userCohorts;
    }

}
