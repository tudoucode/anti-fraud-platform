package com.antifraud.service;

import com.antifraud.entity.SysUser;
import com.antifraud.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiskAssessmentService {

    @Autowired
    private SysUserMapper userMapper;

    /**
     * 更新用户的风险等级
     * 算法逻辑:
     * 1. 基础分: 答题错误率 (权重 60%)
     * 2. 行为分: 是否浏览过高危案例 (权重 40%)
     * @param userId 用户ID
     * @param quizCorrectRate 最近一次答题正确率 (0.0 - 1.0)
     */
    public void updateUserRiskProfile(Long userId, double quizCorrectRate) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) return;

        // 1. 计算风险分 (满分100，分数越高越危险)
        // 正确率越低，风险越高。例如正确率 0.8，则这部分风险分为 (1-0.8)*100 = 20
        int quizRiskScore = (int) ((1.0 - quizCorrectRate) * 100);

        // 简化模型，假设基础分为 quizRiskScore
        int totalRiskScore = quizRiskScore;

        // 2. 判定等级
        int newRiskLevel = 0; // 默认低风险
        if (totalRiskScore > 80) {
            newRiskLevel = 2; // 高风险 (红色)
        } else if (totalRiskScore > 50) {
            newRiskLevel = 1; // 中风险 (黄色)
        }

        // 3. 更新数据库 - 报错的 setRiskScore 和 setRiskLevel 在这里被调用
        user.setRiskScore(totalRiskScore); // Lombok生成的Setter
        user.setRiskLevel(newRiskLevel);   // Lombok生成的Setter
        userMapper.updateById(user);

        // 4. 高风险预警
        if (newRiskLevel == 2) {
            notifyGuardian(user);
        }
    }

    private void notifyGuardian(SysUser user) {
        // 报错的 getUsername 和 getGuardianPhone 在这里被调用
        System.out.println("【警告】发送短信给监护人 " + user.getGuardianPhone() // Lombok生成的Getter
                + "：您的家属 " + user.getUsername() + " 近期反诈意识薄弱，请注意提醒！"); // Lombok生成的Getter
    }
}