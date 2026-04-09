package com.antifraud.dto;

import lombok.Data;

import java.util.List;

@Data
public class RealtimeAnalysisResponse {
    private Integer riskScore;
    private String riskLevel;
    private List<String> suspiciousPoints;
    private List<String> saferReplyOptions;
    private String recommendedAction;
    private boolean suggestGuardianAlert;
    private String summary;
    private String provider;
    private boolean mockMode;
}
