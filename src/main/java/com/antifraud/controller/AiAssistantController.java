package com.antifraud.controller;

import com.antifraud.common.Result;
import com.antifraud.dto.RealtimeAnalysisRequest;
import com.antifraud.dto.RealtimeAnalysisResponse;
import com.antifraud.dto.SimulationStartRequest;
import com.antifraud.dto.SimulationStartResponse;
import com.antifraud.dto.SimulationTurnRequest;
import com.antifraud.dto.SimulationTurnResponse;
import com.antifraud.service.AiAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai-assistant")
@CrossOrigin
public class AiAssistantController {

    @Autowired
    private AiAssistantService aiAssistantService;

    @PostMapping("/simulation/start")
    public Result<SimulationStartResponse> startSimulation(@RequestBody SimulationStartRequest request) {
        return Result.success(aiAssistantService.startSimulation(request));
    }

    @PostMapping("/simulation/respond")
    public Result<SimulationTurnResponse> respondSimulation(@RequestBody SimulationTurnRequest request) {
        return Result.success(aiAssistantService.replyInSimulation(request));
    }

    @PostMapping("/realtime-analysis")
    public Result<RealtimeAnalysisResponse> analyzeRealtimeCall(@RequestBody RealtimeAnalysisRequest request) {
        return Result.success(aiAssistantService.analyzeRealtimeTranscript(request));
    }
}
