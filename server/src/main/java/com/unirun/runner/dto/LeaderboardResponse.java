package com.unirun.runner.dto;

import java.util.List;

public class LeaderboardResponse {

    private String scope;
    private List<LeaderboardEntry> entries;

    public LeaderboardResponse(String scope, List<LeaderboardEntry> entries) {
        this.scope = scope;
        this.entries = entries;
    }

    public String getScope() {
        return scope;
    }

    public List<LeaderboardEntry> getEntries() {
        return entries;
    }
}
