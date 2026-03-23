package com.unirun.runner.service;

import com.unirun.runner.dto.LeaderboardEntry;
import com.unirun.runner.dto.LeaderboardResponse;
import com.unirun.runner.dto.LeaderboardScope;
import com.unirun.runner.entity.UserProfile;
import com.unirun.runner.repository.RunRecordRepository;
import com.unirun.runner.repository.RunRecordRepository.LeaderboardProjection;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderboardService {

    private final RunRecordRepository repository;

    public LeaderboardService(RunRecordRepository repository) {
        this.repository = repository;
    }

    public LeaderboardResponse fetch(UserProfile currentUser, String scopeValue, String school) {
        LeaderboardScope scope = LeaderboardScope.from(scopeValue);
        LocalDate today = LocalDate.now();
        ZoneId zone = ZoneId.systemDefault();
        Instant start;
        Instant end = Instant.now();
        if (scope == LeaderboardScope.DAY) {
            start = today.atStartOfDay(zone).toInstant();
        } else {
            start = today.withDayOfMonth(1).atStartOfDay(zone).toInstant();
        }
        List<LeaderboardProjection> rows = repository.leaderboard(start, end, school);
        List<LeaderboardEntry> entries = new ArrayList<>();
        int rank = 1;
        for (LeaderboardProjection row : rows) {
            boolean isSelf = currentUser != null && currentUser.getId().equals(row.getUserId());
            entries.add(new LeaderboardEntry(
                    row.getNickname(),
                    row.getAvatarUrl(),
                    row.getSchool(),
                    row.getTotalDistance(),
                    isSelf,
                    rank++
            ));
        }
        return new LeaderboardResponse(scope.name(), entries);
    }
}
