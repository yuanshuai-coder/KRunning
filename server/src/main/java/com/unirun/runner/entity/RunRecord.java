package com.unirun.runner.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "run_records", indexes = {
        @Index(name = "idx_run_user", columnList = "user_id"),
        @Index(name = "idx_run_start", columnList = "start_time"),
        @Index(name = "idx_run_end", columnList = "end_time")
})
public class RunRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfile user;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", nullable = false)
    private RunMode mode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RunStatus status = RunStatus.COMPLETED;

    @Column(name = "target_distance_m")
    private Integer targetDistanceMeters;

    @Column(name = "distance_m", nullable = false)
    private Integer distanceMeters;

    @Column(name = "duration_s", nullable = false)
    private Integer durationSeconds;

    @Column(name = "avg_pace", nullable = false)
    private Double avgPace;

    @Column(name = "calories")
    private Integer calories;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Convert(converter = GpsTrackConverter.class)
    @Column(name = "gps_track", columnDefinition = "json")
    private List<GpsPoint> gpsTrack = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public RunMode getMode() {
        return mode;
    }

    public void setMode(RunMode mode) {
        this.mode = mode;
    }

    public RunStatus getStatus() {
        return status;
    }

    public void setStatus(RunStatus status) {
        this.status = status;
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

    public Double getAvgPace() {
        return avgPace;
    }

    public void setAvgPace(Double avgPace) {
        this.avgPace = avgPace;
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

    public List<GpsPoint> getGpsTrack() {
        return gpsTrack;
    }

    public void setGpsTrack(List<GpsPoint> gpsTrack) {
        this.gpsTrack = gpsTrack;
    }
}
