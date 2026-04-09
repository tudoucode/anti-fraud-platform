package com.antifraud.service;

import com.antifraud.config.AiAssistantProperties;
import com.antifraud.dto.RealtimeAnalysisRequest;
import com.antifraud.dto.RealtimeAnalysisResponse;
import com.antifraud.dto.SimulationStartRequest;
import com.antifraud.dto.SimulationStartResponse;
import com.antifraud.dto.SimulationTurnRequest;
import com.antifraud.dto.SimulationTurnResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AiAssistantService {

    private static final String SYSTEM_PROMPT = "你是一名专注于老年人防诈骗教育的 AI 助手。"
            + "你的任务是帮助用户识别风险、提升反诈意识，并始终给出安全、克制、可执行的建议。"
            + "输出内容必须适老化、通俗、稳重。";

    @Autowired
    private AiPromptStrategyService promptStrategyService;

    @Autowired
    private AiModelGatewayService modelGatewayService;

    @Autowired
    private AiAssistantProperties properties;

    @Autowired
    private ObjectMapper objectMapper;

    public SimulationStartResponse startSimulation(SimulationStartRequest request) {
        String sessionId = UUID.randomUUID().toString();
        SimulationStartResponse response;

        if (properties.isReady()) {
            try {
                String modelOutput = modelGatewayService.chat(SYSTEM_PROMPT,
                        promptStrategyService.buildSimulationStartPrompt(request));
                Map<String, Object> json = objectMapper.readValue(modelOutput, new TypeReference<Map<String, Object>>() {});
                response = new SimulationStartResponse();
                response.setSceneDescription(readString(json, "sceneDescription",
                        "一名冒充平台客服的骗子联系老人，声称其账户存在异常交易。"));
                response.setScammerOpening(readString(json, "scammerOpening",
                        "您好，这里是平台客服，您名下有一笔异常订单需要立即核验。"));
                response.setLearningGoals(readStringList(json, "learningGoals",
                        Arrays.asList("识别冒充官方身份", "拒绝泄露验证码", "学会挂断并核实")));
                response.setMockMode(false);
            } catch (Exception e) {
                response = buildMockSimulationStart(request);
            }
        } else {
            response = buildMockSimulationStart(request);
        }

        response.setSessionId(sessionId);
        response.setScamType(defaultValue(request.getScamType(), "冒充客服退款"));
        response.setSceneDifficulty(defaultValue(request.getSceneDifficulty(), "中等"));
        response.setProvider(properties.getProviderName());
        return response;
    }

    public SimulationTurnResponse replyInSimulation(SimulationTurnRequest request) {
        SimulationTurnResponse response;

        if (properties.isReady()) {
            try {
                String modelOutput = modelGatewayService.chat(SYSTEM_PROMPT,
                        promptStrategyService.buildSimulationTurnPrompt(request));
                Map<String, Object> json = objectMapper.readValue(modelOutput, new TypeReference<Map<String, Object>>() {});
                response = new SimulationTurnResponse();
                response.setScammerReply(readString(json, "scammerReply",
                        "阿姨，这个事情比较紧急，您现在先不要告诉别人，按我说的操作就可以。"));
                response.setRiskScore(readInt(json, "riskScore", 75));
                response.setWarningSignals(readStringList(json, "warningSignals",
                        Arrays.asList("对方制造紧迫感", "要求保密", "试图引导继续转账或提供验证码")));
                response.setCoachFeedback(readString(json, "coachFeedback",
                        "这轮对话里最危险的是被对方带节奏。遇到催促、保密和转账要求时，应立即停止交流。"));
                response.setRecommendedAction(readString(json, "recommendedAction",
                        "立即挂断，通过官方客服电话或子女协助核实情况。"));
                response.setConversationShouldEnd(readBoolean(json, "conversationShouldEnd", true));
                response.setMockMode(false);
            } catch (Exception e) {
                response = buildMockSimulationTurn(request);
            }
        } else {
            response = buildMockSimulationTurn(request);
        }

        response.setSessionId(defaultValue(request.getSessionId(), UUID.randomUUID().toString()));
        response.setProvider(properties.getProviderName());
        return response;
    }

    public RealtimeAnalysisResponse analyzeRealtimeTranscript(RealtimeAnalysisRequest request) {
        RealtimeAnalysisResponse response;

        if (properties.isReady()) {
            try {
                String modelOutput = modelGatewayService.chat(SYSTEM_PROMPT,
                        promptStrategyService.buildRealtimeAnalysisPrompt(request));
                Map<String, Object> json = objectMapper.readValue(modelOutput, new TypeReference<Map<String, Object>>() {});
                response = new RealtimeAnalysisResponse();
                response.setRiskScore(readInt(json, "riskScore", 80));
                response.setRiskLevel(readString(json, "riskLevel", "高风险"));
                response.setSuspiciousPoints(readStringList(json, "suspiciousPoints",
                        Arrays.asList("对方自称官方却要求私下处理", "反复催促尽快操作", "涉及验证码、转账或屏幕共享")));
                response.setSaferReplyOptions(readStringList(json, "saferReplyOptions",
                        Arrays.asList("我先挂断，自己联系官方核实", "验证码和银行卡信息我不会提供", "请把通知发到官方渠道")));
                response.setRecommendedAction(readString(json, "recommendedAction",
                        "立即终止通话，不点击链接，不转账，并联系家属或官方平台复核。"));
                response.setSuggestGuardianAlert(readBoolean(json, "suggestGuardianAlert", true));
                response.setSummary(readString(json, "summary",
                        "当前话术符合高频诈骗特征，建议立刻停止互动并寻求家属帮助。"));
                response.setMockMode(false);
            } catch (Exception e) {
                response = buildMockRealtimeAnalysis(request);
            }
        } else {
            response = buildMockRealtimeAnalysis(request);
        }

        response.setProvider(properties.getProviderName());
        return response;
    }

    private SimulationStartResponse buildMockSimulationStart(SimulationStartRequest request) {
        SimulationStartResponse response = new SimulationStartResponse();
        String scamType = defaultValue(request.getScamType(), "冒充客服退款");
        response.setSceneDescription("系统已进入“" + scamType + "”演练模式。诈骗者会以官方身份建立信任，再用紧急措辞诱导老人提供验证码或转账。");
        response.setScammerOpening("您好，我们是平台客服。系统检测到您有一笔异常扣费，如果不马上处理，今天可能会继续扣款。");
        response.setLearningGoals(Arrays.asList("识别“官方身份+紧急处理”组合话术", "拒绝透露验证码、银行卡和身份证信息", "学会挂断电话后通过官方渠道二次核实"));
        response.setMockMode(true);
        return response;
    }

    private SimulationTurnResponse buildMockSimulationTurn(SimulationTurnRequest request) {
        SimulationTurnResponse response = new SimulationTurnResponse();
        String reply = defaultValue(request.getElderReply(), "");
        boolean mentionedCode = reply.contains("验证码") || reply.contains("短信");
        boolean mentionedTransfer = reply.contains("转账") || reply.contains("打钱");
        int riskScore = mentionedCode || mentionedTransfer ? 88 : 68;

        response.setScammerReply("您不用担心，我们现在就是帮您止损。为了马上取消异常业务，请把刚收到的验证码告诉我，或者按我发的链接操作。");
        response.setRiskScore(riskScore);
        response.setWarningSignals(Arrays.asList("骗子继续强调紧急处理", "开始索要验证码或诱导点击链接", "故意阻断老人向家属求证"));
        response.setCoachFeedback("如果顺着对方继续提供信息，风险会快速升高。最稳妥的做法是停止对话，不再解释个人情况。");
        response.setRecommendedAction("直接挂断，并联系官方客服、社区工作人员或子女核实。");
        response.setConversationShouldEnd(riskScore >= 80);
        response.setMockMode(true);
        return response;
    }

    private RealtimeAnalysisResponse buildMockRealtimeAnalysis(RealtimeAnalysisRequest request) {
        RealtimeAnalysisResponse response = new RealtimeAnalysisResponse();
        String transcript = defaultValue(request.getTranscript(), "");
        boolean highRisk = transcript.contains("验证码")
                || transcript.contains("转账")
                || transcript.contains("链接")
                || transcript.contains("账户冻结")
                || transcript.contains("公安");

        response.setRiskScore(highRisk ? 90 : 60);
        response.setRiskLevel(highRisk ? "高风险" : "中风险");
        response.setSuspiciousPoints(Arrays.asList(
                "对方使用官方、公安、客服等身份压迫用户相信其权威性",
                "对话中出现验证码、转账、链接、屏幕共享等高危关键词",
                "对方强调马上处理、否则后果严重，属于典型施压话术"
        ));
        response.setSaferReplyOptions(Arrays.asList(
                "我不在电话里处理这些事情，我会自己联系官方核实",
                "验证码和银行卡信息我不会提供",
                "请等我和家里人确认后再说"
        ));
        response.setRecommendedAction("立刻中断交流，保留通话记录，必要时联系家属或报警咨询。");
        response.setSuggestGuardianAlert(highRisk);
        response.setSummary(highRisk
                ? "当前内容高度疑似诈骗话术，已经触发验证码/转账类高危信号。"
                : "当前对话存在诱导和施压特征，建议不要继续配合对方操作。");
        response.setMockMode(true);
        return response;
    }

    private String readString(Map<String, Object> source, String key, String fallback) {
        Object value = source.get(key);
        return value == null ? fallback : String.valueOf(value);
    }

    private Integer readInt(Map<String, Object> source, String key, Integer fallback) {
        Object value = source.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException ignored) {
                return fallback;
            }
        }
        return fallback;
    }

    private boolean readBoolean(Map<String, Object> source, String key, boolean fallback) {
        Object value = source.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return fallback;
    }

    @SuppressWarnings("unchecked")
    private List<String> readStringList(Map<String, Object> source, String key, List<String> fallback) {
        Object value = source.get(key);
        if (value instanceof List) {
            return ((List<Object>) value).stream().map(String::valueOf).toList();
        }
        return fallback;
    }

    private String defaultValue(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
