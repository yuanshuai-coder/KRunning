package com.unirun.runner.controller;

import com.unirun.runner.dto.StatsSummaryResponse;
import com.unirun.runner.entity.UserProfile;
import com.unirun.runner.service.SessionService;
import com.unirun.runner.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final SessionService sessionService;
    private final StatsService statsService;

    public StatsController(SessionService sessionService, StatsService statsService) {
        this.sessionService = sessionService;
        this.statsService = statsService;
    }

    @GetMapping("/summary")
    public ResponseEntity<StatsSummaryResponse> summary(@RequestHeader("X-Session-Token") String token) {
        UserProfile user = sessionService.requireUser(token);
        return ResponseEntity.ok(statsService.summary(user));
    }
}
