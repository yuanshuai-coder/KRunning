package com.unirun.runner.dto;

import com.unirun.runner.entity.RunMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.util.List;

public class RunCreateRequest {

    @NotNull
    private RunMode mode;

    private Integer targetDistanceMeters;

    @NotNull
    @Positive
    private Integer distanceMeters;

    @NotNull
    @Positive
    private Integer durationSeconds;

    private Integer calories;

    @NotNull
    private Instant startTime;

    @NotNull
    private Instant endTime;

    @Valid
    private List<GpsPointPayload> gpsTrack;

    public RunMode getMode() {
        return mode;
    }

    public void setMode(RunMode mode) {
        this.mode = mode;
    }

    public Integer getTargetDistanceMeters() {
        return targetDistanceMeters;
    }

    public void setTargetDistanceMeters(Integer targetDistanceMeters) {
        this.targetDistanceMeters = targetDistanceMeters;
    }

    public Integer getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(Integer distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public List<GpsPointPayload> getGpsTrack() {
        return gpsTrack;
    }

    public void setGpsTrack(List<GpsPointPayload> gpsTrack) {
        this.gpsTrack = gpsTrack;
    }
}
