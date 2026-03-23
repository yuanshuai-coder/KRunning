package com.unirun.runner.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unirun.runner.config.AppProperties;
import com.unirun.runner.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeChatMiniProgramClient {

    private final RestTemplate restTemplate;
    private final AppProperties properties;

    public WeChatMiniProgramClient(RestTemplate restTemplate, AppProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public String resolveOpenId(String authCode) {
        String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                properties.getWechat().getAppId(),
                properties.getWechat().getAppSecret(),
                authCode
        );
        Response response = restTemplate.getForObject(url, Response.class);
        if (response == null || response.errCode != 0 || response.openId == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "无法获取 openId" + (response != null ? ":" + response.errMsg : ""));
        }
        return response.openId;
    }

    private static final class Response {
        @JsonProperty("openid")
        private String openId;
        @JsonProperty("errcode")
        private int errCode;
        @JsonProperty("errmsg")
        private String errMsg;
    }
}
