package com.antifraud.service;

import com.antifraud.config.AiAssistantProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AiModelGatewayService {

    @Autowired
    private RestTemplate aiAssistantRestTemplate;

    @Autowired
    private AiAssistantProperties properties;

    public String chat(String systemPrompt, String userPrompt) {
        OpenAiChatRequest request = new OpenAiChatRequest();
        request.setModel(properties.getModel());
        request.setTemperature(0.3);
        request.setMessages(buildMessages(systemPrompt, userPrompt));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getApiKey());

        HttpEntity<OpenAiChatRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<OpenAiChatResponse> response = aiAssistantRestTemplate.exchange(
                properties.buildChatUrl(),
                HttpMethod.POST,
                entity,
                OpenAiChatResponse.class
        );

        OpenAiChatResponse body = response.getBody();
        if (body == null || body.getChoices() == null || body.getChoices().isEmpty()) {
            throw new IllegalStateException("AI 服务返回为空");
        }
        OpenAiChatChoice firstChoice = body.getChoices().get(0);
        if (firstChoice.getMessage() == null || firstChoice.getMessage().getContent() == null) {
            throw new IllegalStateException("AI 服务返回内容缺失");
        }
        return firstChoice.getMessage().getContent();
    }

    private List<OpenAiMessage> buildMessages(String systemPrompt, String userPrompt) {
        List<OpenAiMessage> messages = new ArrayList<>();

        OpenAiMessage system = new OpenAiMessage();
        system.setRole("system");
        system.setContent(systemPrompt);
        messages.add(system);

        OpenAiMessage user = new OpenAiMessage();
        user.setRole("user");
        user.setContent(userPrompt);
        messages.add(user);

        return messages;
    }

    @Data
    private static class OpenAiChatRequest {
        private String model;
        private Double temperature;
        private List<OpenAiMessage> messages;
    }

    @Data
    private static class OpenAiMessage {
        private String role;
        private String content;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OpenAiChatResponse {
        private List<OpenAiChatChoice> choices;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OpenAiChatChoice {
        private OpenAiMessage message;
    }
}
