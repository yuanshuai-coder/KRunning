package com.unirun.runner.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final WeChat wechat = new WeChat();
    private final Security security = new Security();

    public WeChat getWechat() {
        return wechat;
    }

    public Security getSecurity() {
        return security;
    }

    public static class WeChat {
        @NotBlank
        private String appId;
        @NotBlank
        private String appSecret;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public void setAppSecret(String appSecret) {
            this.appSecret = appSecret;
        }
    }

    public static class Security {
        @Positive
        private long sessionTtlMinutes = 4320; // 默认 3 天

        public long getSessionTtlMinutes() {
            return sessionTtlMinutes;
        }

        public void setSessionTtlMinutes(long sessionTtlMinutes) {
            this.sessionTtlMinutes = sessionTtlMinutes;
        }
    }
}
