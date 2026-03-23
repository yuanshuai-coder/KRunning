package com.unirun.runner.service;

import com.unirun.runner.config.AppProperties;
import com.unirun.runner.dto.AuthLoginRequest;
import com.unirun.runner.dto.AuthLoginResponse;
import com.unirun.runner.dto.ProfileResponse;
import com.unirun.runner.entity.UserProfile;
import com.unirun.runner.entity.UserSession;
import com.unirun.runner.exception.ApiException;
import com.unirun.runner.repository.UserProfileRepository;
import com.unirun.runner.repository.UserSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final WeChatMiniProgramClient weChatClient;
    private final UserProfileRepository userProfileRepository;
    private final UserSessionRepository sessionRepository;
    private final AppProperties properties;

    public AuthService(WeChatMiniProgramClient weChatClient,
                       UserProfileRepository userProfileRepository,
                       UserSessionRepository sessionRepository,
                       AppProperties properties) {
        this.weChatClient = weChatClient;
        this.userProfileRepository = userProfileRepository;
        this.sessionRepository = sessionRepository;
        this.properties = properties;
    }

    @Transactional
    public AuthLoginResponse login(AuthLoginRequest request) {
        try {
            String openId = weChatClient.resolveOpenId(request.getCode());
            UserProfile profile = userProfileRepository.findByOpenId(openId)
                    .orElseGet(() -> {
                        UserProfile created = new UserProfile();
                        created.setOpenId(openId);
                        created.setVoiceEnabled(true);
                        return created;
                    });
            profile.setNickname(request.getNickname());
            profile.setAvatarUrl(request.getAvatarUrl());
            profile.setSchool(request.getSchool());
            profile.setClassName(request.getClassName());
            profile.setGender(request.getGender());
            profile.setCountry(request.getCountry());
            profile.setProvince(request.getProvince());
            profile.setCity(request.getCity());
            profile.setLastLoginAt(Instant.now());
            userProfileRepository.save(profile);

            UserSession session = new UserSession();
            session.setUser(profile);
            session.setToken(UUID.randomUUID().toString());
            Duration ttl = Duration.ofMinutes(properties.getSecurity().getSessionTtlMinutes());
            session.setExpiresAt(Instant.now().plus(ttl));
            sessionRepository.save(session);

            log.info("[auth] 用户 {} 登录成功，openId={}", profile.getNickname(), profile.getOpenId());
            return new AuthLoginResponse(session.getToken(), ProfileResponse.fromEntity(profile));
        } catch (RuntimeException ex) {
            log.error("[auth] 微信登录失败，code={}, nickname={}, cause={}", request.getCode(), request.getNickname(), ex.getMessage(), ex);
            throw ex;
        }
    }

    public void logout(String token) {
        sessionRepository.findByToken(token).ifPresent(sessionRepository::delete);
    }

    public void validateBuild(int clientBuild) {
        if (clientBuild < 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "客户端版本过低");
        }
    }
}
