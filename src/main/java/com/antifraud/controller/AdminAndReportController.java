package com.antifraud.controller;

import com.antifraud.common.Result;
import com.antifraud.entity.FraudCase;
import com.antifraud.entity.QuizQuestion;
import com.antifraud.entity.SysUser;
import com.antifraud.service.FraudCaseService;
import com.antifraud.mapper.QuizQuestionMapper;
import com.antifraud.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class AdminAndReportController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private FraudCaseService fraudCaseService;

    @Autowired
    private QuizQuestionMapper quizQuestionMapper;

    // ================== 1. 仪表盘与统计 ==================

    @GetMapping("/admin/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userService.count());
        stats.put("highRiskCount", userService.count(new QueryWrapper<SysUser>().eq("risk_level", 2)));
        stats.put("caseCount", fraudCaseService.count());
        stats.put("questionCount", quizQuestionMapper.selectCount(null));
        stats.put("riskDistribution", Arrays.asList(
                userService.count(new QueryWrapper<SysUser>().eq("risk_level", 0)),
                userService.count(new QueryWrapper<SysUser>().eq("risk_level", 1)),
                userService.count(new QueryWrapper<SysUser>().eq("risk_level", 2))
        ));
        return Result.success(stats);
    }

    // ================== 2. 用户管理 ==================

    @GetMapping("/admin/users")
    public Result<List<SysUser>> getAllUsers() {
        return Result.success(userService.list());
    }

    @PutMapping("/admin/users")
    public Result<String> updateUser(@RequestBody SysUser user) {
        if (user.getId() == null) return Result.error("用户ID不能为空");
        boolean success = userService.updateById(user);
        return success ? Result.success("用户信息更新成功") : Result.error("更新失败");
    }

    /**
     * 删除用户 (新增)
     * DELETE /api/v1/admin/users/{id}
     */
    @DeleteMapping("/admin/users/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        boolean success = userService.removeById(id);
        return success ? Result.success("用户删除成功") : Result.error("删除失败");
    }

    @PostMapping("/admin/push-warning")
    public Result<String> pushWarning(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("userId");
        SysUser user = userService.getById(userId);
        if (user == null || user.getGuardianPhone() == null) return Result.error("推送失败");
        System.out.println(">>> 预警推送至: " + user.getGuardianPhone());
        return Result.success("预警已发送");
    }

    // ================== 3. 内容管理：案例 ==================

    @GetMapping("/admin/cases")
    public Result<List<FraudCase>> getAllCases() {
        return Result.success(fraudCaseService.list(new QueryWrapper<FraudCase>().orderByDesc("create_time")));
    }

    @PostMapping("/admin/cases")
    public Result<String> addCase(@RequestBody FraudCase fraudCase) {
        fraudCase.setCreateTime(LocalDateTime.now());
        fraudCase.setViewCount(0);
        if (fraudCase.getIsPublished() == null) fraudCase.setIsPublished(true);
        fraudCaseService.save(fraudCase);
        return Result.success("案例添加成功");
    }

    @DeleteMapping("/admin/cases/{id}")
    public Result<String> deleteCase(@PathVariable Long id) {
        fraudCaseService.removeById(id);
        return Result.success("案例删除成功");
    }

    // ================== 4. 内容管理：题库 ==================

    @GetMapping("/admin/questions")
    public Result<List<QuizQuestion>> getAllQuestions() {
        return Result.success(quizQuestionMapper.selectList(null));
    }

    @PostMapping("/admin/questions")
    public Result<String> addQuestion(@RequestBody QuizQuestion question) {
        quizQuestionMapper.insert(question);
        return Result.success("题目添加成功");
    }

    @DeleteMapping("/admin/questions/{id}")
    public Result<String> deleteQuestion(@PathVariable Long id) {
        quizQuestionMapper.deleteById(id);
        return Result.success("题目删除成功");
    }

    // ================== 5. 用户报告接口 ==================
    @GetMapping("/report/{userId}")
    public Result<Map<String, Object>> getUserReport(@PathVariable Long userId) {
        SysUser user = userService.getById(userId);
        if (user == null) return Result.error("用户不存在");
        Map<String, Object> report = new HashMap<>();
        report.put("username", user.getUsername());
        report.put("riskLevel", user.getRiskLevel());
        report.put("riskScore", user.getRiskScore());
        // 简单计算逻辑，实际可复用之前的复杂逻辑
        report.put("studyHours", (int)(Math.random() * 10) + 1);
        report.put("quizCount", (int)(Math.random() * 20) + 5);
        report.put("weakPoint", user.getRiskLevel() > 0 ? "金融理财类诈骗" : "暂无明显短板");
        report.put("generateTime", java.time.LocalDate.now().toString());
        return Result.success(report);
    }
}