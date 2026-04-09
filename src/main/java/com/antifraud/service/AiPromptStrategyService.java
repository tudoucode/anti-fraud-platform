package com.antifraud.service;

import com.antifraud.dto.AiChatMessage;
import com.antifraud.dto.RealtimeAnalysisRequest;
import com.antifraud.dto.SimulationStartRequest;
import com.antifraud.dto.SimulationTurnRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AiPromptStrategyService {

    public String buildSimulationStartPrompt(SimulationStartRequest request) {
        String scamType = defaultText(request.getScamType(), "冒充客服退款");
        String difficulty = defaultText(request.getSceneDifficulty(), "中等");
        String elderName = defaultText(request.getElderName(), "长者用户");
        String riskPreference = defaultText(request.getRiskPreference(), "谨慎但容易相信官方口吻");

        return "你是适老化防诈教育平台中的反诈训练引擎。"
                + "请生成一个适合老年用户的模拟诈骗演练开场，诈骗类型为：" + scamType + "，难度为：" + difficulty + "。"
                + "受训用户信息：姓名=" + elderName + "，年龄=" + valueOrUnknown(request.getAge()) + "，行为特征=" + riskPreference + "。"
                + "输出必须是 JSON，对象字段固定为：sceneDescription、scammerOpening、learningGoals。"
                + "learningGoals 必须是长度为 3 的字符串数组。"
                + "内容要求口语化、真实但不能包含鼓励违法的细节，不要输出任何额外解释。";
    }

    public String buildSimulationTurnPrompt(SimulationTurnRequest request) {
        String scamType = defaultText(request.getScamType(), "未知诈骗类型");
        String difficulty = defaultText(request.getSceneDifficulty(), "中等");
        StringBuilder historyBuilder = new StringBuilder();
        List<AiChatMessage> history = request.getHistory();
        if (history != null) {
            for (AiChatMessage message : history) {
                if (message == null) {
                    continue;
                }
                historyBuilder.append(defaultText(message.getRole(), "user"))
                        .append(": ")
                        .append(defaultText(message.getContent(), ""))
                        .append("\n");
            }
        }

        return "你是适老化防诈教育平台中的模拟诈骗演练机器人。"
                + "当前演练目标是帮助老年用户识别诈骗，而不是让诈骗成功。"
                + "请基于诈骗类型=" + scamType + "、难度=" + difficulty + "继续一轮对话。"
                + "历史对话如下：\n" + historyBuilder
                + "老年用户本轮回复：" + defaultText(request.getElderReply(), "")
                + "\n请输出 JSON，对象字段固定为：scammerReply、riskScore、warningSignals、coachFeedback、recommendedAction、conversationShouldEnd。"
                + "riskScore 范围 0-100，warningSignals 为长度 2-4 的数组。"
                + "scammerReply 需要保持真实口语风格，但避免提供违法教程。"
                + "coachFeedback 必须指出这轮回复中的风险点并给出更安全做法，不要输出任何额外解释。";
    }

    public String buildRealtimeAnalysisPrompt(RealtimeAnalysisRequest request) {
        StringBuilder messageBuilder = new StringBuilder();
        if (request.getRecentMessages() != null) {
            for (String message : request.getRecentMessages()) {
                messageBuilder.append("- ").append(defaultText(message, "")).append("\n");
            }
        }

        return "你是适老化防诈教育平台中的实时话术分析助手。"
                + "请对以下通话或聊天内容进行防诈骗风险分析。"
                + "来电方身份标签：" + defaultText(request.getCallerRole(), "未知") + "。"
                + "完整转写：" + defaultText(request.getTranscript(), "") + "。"
                + "最近消息片段：\n" + messageBuilder
                + "请输出 JSON，对象字段固定为：riskScore、riskLevel、suspiciousPoints、saferReplyOptions、recommendedAction、suggestGuardianAlert、summary。"
                + "riskLevel 只允许 低风险、中风险、高风险。"
                + "suspiciousPoints 与 saferReplyOptions 各输出 3 条，语言必须适合老年用户阅读，避免术语堆砌，不要输出任何额外解释。";
    }

    private String defaultText(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private String valueOrUnknown(Integer value) {
        return value == null ? "未知" : String.valueOf(value);
    }
}
