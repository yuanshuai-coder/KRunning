package com.unirun.runner.service;

import com.unirun.runner.dto.ProfileResponse;
import com.unirun.runner.dto.ProfileUpdateRequest;
import com.unirun.runner.entity.UserProfile;
import com.unirun.runner.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final UserProfileRepository repository;

    public ProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ProfileResponse update(UserProfile user, ProfileUpdateRequest request) {
        user.setNickname(request.getNickname());
        user.setAvatarUrl(request.getAvatarUrl());
        user.setSchool(request.getSchool());
        user.setClassName(request.getClassName());
        if (request.getVoiceEnabled() != null) {
            user.setVoiceEnabled(request.getVoiceEnabled());
        }
        repository.save(user);
        return ProfileResponse.fromEntity(user);
    }
}
