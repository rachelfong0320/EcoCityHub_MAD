package com.example.ecocity;

public class leaderboardHelper {
    private String username;
    private int point;
    private String ranking;

    // Required default (no-argument) constructor for Firebase
    public leaderboardHelper() {
    }

    public leaderboardHelper(String username, int point, String ranking) {
        this.username = username;
        this.point = point;
        this.ranking = ranking;
    }

    // Getters and setters for the fields
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }
}
