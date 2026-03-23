package com.unirun.runner.dto;

import com.unirun.runner.entity.GpsPoint;
import com.unirun.runner.entity.RunRecord;

import java.util.List;

public class RunDetailResponse {

    private RunSummaryResponse summary;
    private List<GpsPoint> gpsTrack;

    public static RunDetailResponse fromEntity(RunRecord record) {
        RunDetailResponse response = new RunDetailResponse();
        response.summary = RunSummaryResponse.fromEntity(record);
        response.gpsTrack = record.getGpsTrack();
        return response;
    }

    public RunSummaryResponse getSummary() {
        return summary;
    }

    public List<GpsPoint> getGpsTrack() {
        return gpsTrack;
    }
}
