package com.antifraud.service;

import com.antifraud.entity.FraudCase;
import com.antifraud.mapper.FraudCaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FraudCaseService extends ServiceImpl<FraudCaseMapper, FraudCase> {

    /**
     * 获取已发布的案例列表
     */
    public List<FraudCase> getPublishedCases() {
        QueryWrapper<FraudCase> queryWrapper = new QueryWrapper<>();
        // 注意：数据库中的 is_published 字段必须为 1 才能查出
        queryWrapper.eq("is_published", true)
                .orderByDesc("create_time");
        return list(queryWrapper);
    }
}