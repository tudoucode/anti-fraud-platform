package com.antifraud.dto;

import lombok.Data;

import java.util.List;

@Data
public class SimulationTurnResponse {
    private String sessionId;
    private String scammerReply;
    private Integer riskScore;
    private List<String> warningSignals;
    private String coachFeedback;
    private String recommendedAction;
    private boolean conversationShouldEnd;
    private String provider;
    private boolean mockMode;
}
