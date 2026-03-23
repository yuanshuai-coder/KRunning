package com.unirun.runner.controller;

import com.unirun.runner.dto.ProfileResponse;
import com.unirun.runner.dto.ProfileUpdateRequest;
import com.unirun.runner.entity.UserProfile;
import com.unirun.runner.service.ProfileService;
import com.unirun.runner.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final SessionService sessionService;
    private final ProfileService profileService;

    public ProfileController(SessionService sessionService, ProfileService profileService) {
        this.sessionService = sessionService;
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> profile(@RequestHeader("X-Session-Token") String token) {
        UserProfile user = sessionService.requireUser(token);
        return ResponseEntity.ok(ProfileResponse.fromEntity(user));
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> update(@RequestHeader("X-Session-Token") String token,
                                                  @Valid @RequestBody ProfileUpdateRequest request) {
        UserProfile user = sessionService.requireUser(token);
        return ResponseEntity.ok(profileService.update(user, request));
    }
}
