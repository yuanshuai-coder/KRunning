package com.unirun.runner.dto;

public class AuthLoginResponse {

    private String sessionToken;
    private ProfileResponse profile;

    public AuthLoginResponse(String sessionToken, ProfileResponse profile) {
        this.sessionToken = sessionToken;
        this.profile = profile;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public ProfileResponse getProfile() {
        return profile;
    }
}
