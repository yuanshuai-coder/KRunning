package com.unirun.runner.dto;

import jakarta.validation.constraints.NotBlank;

public class ProfileUpdateRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    private String avatarUrl;

    private String school;

    private String className;

    private Boolean voiceEnabled;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Boolean getVoiceEnabled() {
        return voiceEnabled;
    }

    public void setVoiceEnabled(Boolean voiceEnabled) {
        this.voiceEnabled = voiceEnabled;
    }
}
