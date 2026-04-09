package com.antifraud.controller;

import com.antifraud.common.Result;
import com.antifraud.entity.QuizQuestion;
import com.antifraud.mapper.QuizQuestionMapper;
import com.antifraud.service.RiskAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/quiz")
@CrossOrigin
public class QuizController {

    @Autowired
    private QuizQuestionMapper quizMapper;

    @Autowired
    private RiskAssessmentService riskService;

    /**
     * 获取随机题目 (改为随机抽取 10 道)
     * GET /api/v1/quiz/questions
     */
    @GetMapping("/questions")
    public Result<List<QuizQuestion>> getQuestions() {
        // 1. 获取数据库中所有题目
        List<QuizQuestion> allQuestions = quizMapper.selectList(null);

        // 2. 随机打乱顺序
        Collections.shuffle(allQuestions);

        // 3. 截取前 10 道题 (如果不足10道则返回全部)
        int limit = Math.min(allQuestions.size(), 10);
        List<QuizQuestion> randomQuestions = allQuestions.stream()
                .limit(limit)
                .collect(Collectors.toList());

        return Result.success(randomQuestions);
    }

    /**
     * 提交测试结果并计算风险
     */
    @PostMapping("/submit")
    public Result<String> submitQuiz(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            Double score = Double.valueOf(payload.get("score").toString());

            riskService.updateUserRiskProfile(userId, score);

            return Result.success("测评完成，风险等级已动态更新");
        } catch (Exception e) {
            return Result.error("提交失败：" + e.getMessage());
        }
    }
}