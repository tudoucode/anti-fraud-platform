package com.antifraud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.ai.assistant")
public class AiAssistantProperties {
    private boolean enabled;
    private String providerName = "OpenAI-Compatible";
    private String baseUrl = "https://api.openai.com";
    private String chatPath = "/v1/chat/completions";
    private String model = "gpt-4o-mini";
    private String apiKey;
    private int connectTimeoutMs = 5000;
    private int readTimeoutMs = 20000;

    public String buildChatUrl() {
        if (baseUrl == null) {
            return chatPath;
        }
        if (chatPath == null) {
            return baseUrl;
        }
        boolean baseEndsWithSlash = baseUrl.endsWith("/");
        boolean pathStartsWithSlash = chatPath.startsWith("/");
        if (baseEndsWithSlash && pathStartsWithSlash) {
            return baseUrl + chatPath.substring(1);
        }
        if (!baseEndsWithSlash && !pathStartsWithSlash) {
            return baseUrl + "/" + chatPath;
        }
        return baseUrl + chatPath;
    }

    public boolean isReady() {
        return enabled && apiKey != null && !apiKey.isBlank();
    }
}
