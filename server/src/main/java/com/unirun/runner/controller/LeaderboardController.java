package com.unirun.runner.controller;

import com.unirun.runner.dto.LeaderboardResponse;
import com.unirun.runner.entity.UserProfile;
import com.unirun.runner.service.LeaderboardService;
import com.unirun.runner.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final SessionService sessionService;
    private final LeaderboardService leaderboardService;

    public LeaderboardController(SessionService sessionService, LeaderboardService leaderboardService) {
        this.sessionService = sessionService;
        this.leaderboardService = leaderboardService;
    }

    @GetMapping
    public ResponseEntity<LeaderboardResponse> leaderboard(@RequestHeader("X-Session-Token") String token,
                                                           @RequestParam(defaultValue = "DAY") String scope,
                                                           @RequestParam(required = false) String school) {
        UserProfile user = sessionService.requireUser(token);
        return ResponseEntity.ok(leaderboardService.fetch(user, scope, school));
    }
}
