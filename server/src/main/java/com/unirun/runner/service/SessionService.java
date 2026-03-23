package com.unirun.runner.service;

import com.unirun.runner.entity.UserProfile;
import com.unirun.runner.entity.UserSession;
import com.unirun.runner.exception.ApiException;
import com.unirun.runner.repository.UserSessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class SessionService {

    private final UserSessionRepository sessionRepository;

    public SessionService(UserSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public UserProfile requireUser(String token) {
        if (token == null || token.isBlank()) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "缺少会话 token");
        }
        UserSession session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "会话无效"));
        if (session.getExpiresAt().isBefore(Instant.now())) {
            sessionRepository.delete(session);
            throw new ApiException(HttpStatus.UNAUTHORIZED, "会话已过期");
        }
        return session.getUser();
    }
}
