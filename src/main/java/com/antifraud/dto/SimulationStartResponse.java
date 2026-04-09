package com.antifraud.dto;

import lombok.Data;

import java.util.List;

@Data
public class SimulationStartResponse {
    private String sessionId;
    private String scamType;
    private String sceneDifficulty;
    private String sceneDescription;
    private String scammerOpening;
    private List<String> learningGoals;
    private String provider;
    private boolean mockMode;
}
