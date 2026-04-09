package com.antifraud.controller;

import com.antifraud.common.Result;
import com.antifraud.entity.FraudCase;
import com.antifraud.service.FraudCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cases")
@CrossOrigin // 允许前端跨域访问
public class FraudCaseController {

    @Autowired
    private FraudCaseService fraudCaseService;

    /**
     * 获取案例列表
     * 访问地址: http://localhost:8080/api/v1/cases/list
     */
    @GetMapping("/list")
    public Result<List<FraudCase>> getCaseList() {
        List<FraudCase> list = fraudCaseService.getPublishedCases();
        return Result.success(list);
    }

    /**
     * 获取案例详情
     */
    @GetMapping("/{id}")
    public Result<FraudCase> getCaseDetails(@PathVariable Long id) {
        FraudCase fraudCase = fraudCaseService.getById(id);
        if (fraudCase == null) {
            return Result.error("找不到该案例");
        }
        return Result.success(fraudCase);
    }
}
