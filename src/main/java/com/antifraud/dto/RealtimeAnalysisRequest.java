package com.antifraud.dto;

import lombok.Data;

import java.util.List;

@Data
public class RealtimeAnalysisRequest {
    private Long userId;
    private String callerRole;
    private String transcript;
    private List<String> recentMessages;
}
