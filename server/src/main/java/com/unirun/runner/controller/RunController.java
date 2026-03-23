package com.unirun.runner.controller;

import com.unirun.runner.dto.PageResponse;
import com.unirun.runner.dto.RunCreateRequest;
import com.unirun.runner.dto.RunDetailResponse;
import com.unirun.runner.dto.RunSummaryResponse;
import com.unirun.runner.entity.UserProfile;
import com.unirun.runner.service.RunService;
import com.unirun.runner.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/runs")
public class RunController {

    private final SessionService sessionService;
    private final RunService runService;

    public RunController(SessionService sessionService, RunService runService) {
        this.sessionService = sessionService;
        this.runService = runService;
    }

    @PostMapping
    public ResponseEntity<RunSummaryResponse> record(@RequestHeader("X-Session-Token") String token,
                                                     @Valid @RequestBody RunCreateRequest request) {
        UserProfile user = sessionService.requireUser(token);
        return ResponseEntity.ok(runService.record(user, request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<RunSummaryResponse>> history(@RequestHeader("X-Session-Token") String token,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        UserProfile user = sessionService.requireUser(token);
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);
        Page<RunSummaryResponse> history = runService.history(user, safePage, safeSize);
        return ResponseEntity.ok(PageResponse.fromPage(history));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RunDetailResponse> detail(@RequestHeader("X-Session-Token") String token,
                                                    @PathVariable Long id) {
        UserProfile user = sessionService.requireUser(token);
        return ResponseEntity.ok(runService.detail(user, id));
    }
}
