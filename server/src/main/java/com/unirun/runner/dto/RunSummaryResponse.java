package com.unirun.runner.dto;

import com.unirun.runner.entity.RunRecord;
import com.unirun.runner.entity.RunStatus;

import java.time.Instant;

public class RunSummaryResponse {

    private Long id;
    private String mode;
    private Integer distanceMeters;
    private Integer durationSeconds;
    private Double avgPace;
    private Instant startTime;
    private Instant endTime;
    private RunStatus status;

    public static RunSummaryResponse fromEntity(RunRecord record) {
        RunSummaryResponse response = new RunSummaryResponse();
        response.id = record.getId();
        response.mode = record.getMode().name();
        response.distanceMeters = record.getDistanceMeters();
        response.durationSeconds = record.getDurationSeconds();
        response.avgPace = record.getAvgPace();
        response.startTime = record.getStartTime();
        response.endTime = record.getEndTime();
        response.status = record.getStatus();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getMode() {
        return mode;
    }

    public Integer getDistanceMeters() {
        return distanceMeters;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public Double getAvgPace() {
        return avgPace;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public RunStatus getStatus() {
        return status;
    }
}
