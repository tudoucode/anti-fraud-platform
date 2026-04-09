# AI 智能反诈助手集成说明

本次扩展为平台新增了两类 AI 能力：

1. 模拟诈骗场景演练
2. 实时话术分析

## 新增接口

### 1. 开始模拟演练

- 路径：`POST /api/v1/ai-assistant/simulation/start`

请求示例：

```json
{
  "userId": 1,
  "elderName": "张阿姨",
  "age": 67,
  "riskPreference": "容易相信官方客服",
  "scamType": "冒充客服退款",
  "sceneDifficulty": "中等"
}
```

### 2. 演练对话续写

- 路径：`POST /api/v1/ai-assistant/simulation/respond`

请求示例：

```json
{
  "sessionId": "demo-session-id",
  "scamType": "冒充客服退款",
  "sceneDifficulty": "中等",
  "elderReply": "那我要怎么处理？",
  "history": [
    {
      "role": "assistant",
      "content": "您好，我们是平台客服，您有一笔异常扣费需要处理。"
    }
  ]
}
```

### 3. 实时话术分析

- 路径：`POST /api/v1/ai-assistant/realtime-analysis`

请求示例：

```json
{
  "userId": 1,
  "callerRole": "冒充公安",
  "transcript": "您涉嫌洗钱，需要马上把资金转到安全账户。",
  "recentMessages": [
    "请不要告诉家里人",
    "现在马上操作"
  ]
}
```

## 配置方式

`src/main/resources/application.yml` 中新增：

```yaml
app:
  ai:
    assistant:
      enabled: false
      provider-name: OpenAI-Compatible
      base-url: https://api.openai.com
      chat-path: /v1/chat/completions
      model: gpt-4o-mini
      api-key: ${AI_API_KEY:}
```

说明：

- `enabled=false` 时，系统会进入本地 mock 模式，仍然能返回演示数据。
- `enabled=true` 且提供 `api-key` 后，会调用 OpenAI 兼容接口。
- 如果后续接入阿里百炼、通义千问、DeepSeek 或其他兼容网关，一般只需要改 `base-url`、`model` 和 `api-key`。

## 设计说明

- `AiPromptStrategyService`：封装防诈骗 Prompt 策略，区分演练开场、演练续写、实时分析三种任务。
- `AiModelGatewayService`：封装大模型 HTTP 调用，采用 OpenAI 兼容协议。
- `AiAssistantService`：统一业务编排，并在未配置 API 或解析失败时自动降级到 mock 结果。
- `AiAssistantController`：对前端暴露 REST 接口。

## 适合项目答辩时强调的点

- 兼容真实大模型接入，不是写死某一家厂商。
- 对老年用户做了适老化输出约束，避免复杂术语。
- 支持无密钥演示模式，方便课程设计、论文展示和本地验收。
- 实时分析与模拟演练共用统一 Prompt 策略，便于后续扩展知识库检索和用户画像增强。
