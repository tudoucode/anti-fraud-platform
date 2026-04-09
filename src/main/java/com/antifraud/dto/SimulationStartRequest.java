package com.antifraud.dto;

import lombok.Data;

@Data
public class SimulationStartRequest {
    private Long userId;
    private String elderName;
    private Integer age;
    private String riskPreference;
    private String scamType;
    private String sceneDifficulty;
}
