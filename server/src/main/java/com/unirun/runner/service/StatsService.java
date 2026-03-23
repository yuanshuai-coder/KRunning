package com.unirun.runner.service;

import com.unirun.runner.dto.StatsSummaryResponse;
import com.unirun.runner.entity.UserProfile;
import com.unirun.runner.repository.RunRecordRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    private final RunRecordRepository repository;

    public StatsService(RunRecordRepository repository) {
        this.repository = repository;
    }

    public StatsSummaryResponse summary(UserProfile user) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zone);
        Instant now = Instant.now();
        Instant dayStart = today.atStartOfDay(zone).toInstant();
        Instant weekStart = today.minusDays(6).atStartOfDay(zone).toInstant();
        Instant monthStart = today.withDayOfMonth(1).atStartOfDay(zone).toInstant();

        long todayDistance = defaultZero(repository.sumDistanceByUserAndPeriod(user, dayStart, now));
        long weekDistance = defaultZero(repository.sumDistanceByUserAndPeriod(user, weekStart, now));
        long monthDistance = defaultZero(repository.sumDistanceByUserAndPeriod(user, monthStart, now));
        long totalRuns = repository.countByUser(user);
        long activeDays = defaultZero(repository.countActiveDays(user));

        List<Object[]> rows = repository.aggregateDailyDistance(user, weekStart, now);
        Map<LocalDate, Long> aggregated = new HashMap<>();
        for (Object[] row : rows) {
            if (row.length < 2 || row[0] == null) {
                continue;
            }
            LocalDate date = toLocalDate(row[0], zone);
            long value = row[1] == null ? 0L : ((Number) row[1]).longValue();
            aggregated.put(date, value);
        }

        List<StatsSummaryResponse.DistancePoint> points = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            points.add(new StatsSummaryResponse.DistancePoint(date, aggregated.getOrDefault(date, 0L)));
        }

        return new StatsSummaryResponse(todayDistance, weekDistance, monthDistance, totalRuns, activeDays, points);
    }

    private long defaultZero(Long value) {
        return value == null ? 0L : value;
    }

    private LocalDate toLocalDate(Object value, ZoneId zone) {
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        if (value instanceof Date date) {
            return date.toLocalDate();
        }
        if (value instanceof java.util.Date utilDate) {
            return Instant.ofEpochMilli(utilDate.getTime()).atZone(zone).toLocalDate();
        }
        if (value instanceof Instant instant) {
            return instant.atZone(zone).toLocalDate();
        }
        throw new IllegalArgumentException("Unsupported date projection type: " + value);
    }
}
