package com.antifraud.dto;

import lombok.Data;

import java.util.List;

@Data
public class SimulationTurnRequest {
    private String sessionId;
    private String scamType;
    private String sceneDifficulty;
    private String elderReply;
    private List<AiChatMessage> history;
}
