package com.unirun.runner.dto;

import com.unirun.runner.entity.UserProfile;

public class ProfileResponse {

    private Long id;
    private String nickname;
    private String avatarUrl;
    private String school;
    private String className;
    private boolean voiceEnabled;
    private String gender;
    private String country;
    private String province;
    private String city;
    private String lastLoginAt;

    public static ProfileResponse fromEntity(UserProfile profile) {
        ProfileResponse response = new ProfileResponse();
        response.id = profile.getId();
        response.nickname = profile.getNickname();
        response.avatarUrl = profile.getAvatarUrl();
        response.school = profile.getSchool();
        response.className = profile.getClassName();
        response.voiceEnabled = profile.isVoiceEnabled();
        response.gender = profile.getGender();
        response.country = profile.getCountry();
        response.province = profile.getProvince();
        response.city = profile.getCity();
        response.lastLoginAt = profile.getLastLoginAt() != null ? profile.getLastLoginAt().toString() : null;
        return response;
    }

    public Long getId() {
        return id;
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

    public String getClassName() {
        return className;
    }

    public boolean isVoiceEnabled() {
        return voiceEnabled;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getLastLoginAt() {
        return lastLoginAt;
    }
}
