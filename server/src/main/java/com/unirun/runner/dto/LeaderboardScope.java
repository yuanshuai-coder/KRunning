package com.unirun.runner.dto;

public enum LeaderboardScope {
    DAY,
    MONTH;

    public static LeaderboardScope from(String value) {
        if (value == null) {
            return DAY;
        }
        try {
            return LeaderboardScope.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return DAY;
        }
    }
}
