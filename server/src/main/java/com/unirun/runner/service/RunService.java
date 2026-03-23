package com.unirun.runner.service;

import com.unirun.runner.dto.GpsPointPayload;
import com.unirun.runner.dto.RunCreateRequest;
import com.unirun.runner.dto.RunDetailResponse;
import com.unirun.runner.dto.RunSummaryResponse;
import com.unirun.runner.entity.GpsPoint;
import com.unirun.runner.entity.RunRecord;
import com.unirun.runner.entity.UserProfile;
import com.unirun.runner.exception.ApiException;
import com.unirun.runner.repository.RunRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RunService {

    private final RunRecordRepository repository;

    public RunService(RunRecordRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RunSummaryResponse record(UserProfile user, RunCreateRequest request) {
        RunRecord record = new RunRecord();
        record.setUser(user);
        record.setMode(request.getMode());
        record.setTargetDistanceMeters(request.getTargetDistanceMeters());
        record.setDistanceMeters(request.getDistanceMeters());
        record.setDurationSeconds(request.getDurationSeconds());
        record.setCalories(request.getCalories());
        record.setStartTime(request.getStartTime());
        record.setEndTime(request.getEndTime());
        record.setAvgPace(calculatePace(request.getDurationSeconds(), request.getDistanceMeters()));
        record.setGpsTrack(mapTrack(request.getGpsTrack()));
        repository.save(record);
        return RunSummaryResponse.fromEntity(record);
    }

    public Page<RunSummaryResponse> history(UserProfile user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RunRecord> records = repository.findByUserOrderByStartTimeDesc(user, pageable);
        return records.map(RunSummaryResponse::fromEntity);
    }

    public RunDetailResponse detail(UserProfile user, Long id) {
        RunRecord record = repository.findById(id)
                .filter(r -> r.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "记录不存在"));
        return RunDetailResponse.fromEntity(record);
    }

    private Double calculatePace(Integer durationSeconds, Integer distanceMeters) {
        if (distanceMeters == null || distanceMeters == 0) {
            return 0d;
        }
        double minutes = durationSeconds / 60.0;
        double distanceKm = distanceMeters / 1000.0;
        return minutes / distanceKm;
    }

    private List<GpsPoint> mapTrack(List<GpsPointPayload> track) {
        if (track == null) {
            return List.of();
        }
        return track.stream()
                .map(p -> new GpsPoint(p.getLatitude(), p.getLongitude(), p.getSpeed()))
                .toList();
    }
}
