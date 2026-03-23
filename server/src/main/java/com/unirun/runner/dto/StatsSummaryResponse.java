package com.unirun.runner.dto;

import java.time.LocalDate;
import java.util.List;

public class StatsSummaryResponse {

    private Long todayDistanceMeters;
    private Long weekDistanceMeters;
    private Long monthDistanceMeters;
    private Long totalRuns;
    private Long activeDays;
    private List<DistancePoint> chart;

    public StatsSummaryResponse(Long todayDistanceMeters, Long weekDistanceMeters, Long monthDistanceMeters, Long totalRuns, Long activeDays, List<DistancePoint> chart) {
        this.todayDistanceMeters = todayDistanceMeters;
        this.weekDistanceMeters = weekDistanceMeters;
        this.monthDistanceMeters = monthDistanceMeters;
        this.totalRuns = totalRuns;
        this.activeDays = activeDays;
        this.chart = chart;
    }

    public Long getTodayDistanceMeters() {
        return todayDistanceMeters;
    }

    public Long getWeekDistanceMeters() {
        return weekDistanceMeters;
    }

    public Long getMonthDistanceMeters() {
        return monthDistanceMeters;
    }

    public Long getTotalRuns() {
        return totalRuns;
    }

    public Long getActiveDays() {
        return activeDays;
    }

    public List<DistancePoint> getChart() {
        return chart;
    }

    public static class DistancePoint {
        private LocalDate day;
        private Long distanceMeters;

        public DistancePoint(LocalDate day, Long distanceMeters) {
            this.day = day;
            this.distanceMeters = distanceMeters;
        }

        public LocalDate getDay() {
            return day;
        }

        public Long getDistanceMeters() {
            return distanceMeters;
        }
    }
}
