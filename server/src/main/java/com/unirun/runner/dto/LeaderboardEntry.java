package com.unirun.runner.dto;

public class LeaderboardEntry {

    private String nickname;
    private String avatarUrl;
    private String school;
    private Long totalDistanceMeters;
    private boolean isSelf;
    private int rank;

    public LeaderboardEntry(String nickname, String avatarUrl, String school, Long totalDistanceMeters, boolean isSelf, int rank) {
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.school = school;
        this.totalDistanceMeters = totalDistanceMeters;
        this.isSelf = isSelf;
        this.rank = rank;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getSchool() {
        return school;
    }

    public Long getTotalDistanceMeters() {
        return totalDistanceMeters;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public int getRank() {
        return rank;
    }
}
