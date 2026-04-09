package com.antifraud.controller;

import com.antifraud.common.Result;
import com.antifraud.entity.SysUser;
import com.antifraud.service.SysUserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
public class SysUserController {

    @Autowired
    private SysUserService userService;

    /**
     * 简易登录接口
     */
    @PostMapping("/login")
    public Result<SysUser> login(@RequestBody LoginRequest request) {
        SysUser user = userService.getByPhone(request.getPhone());
        if (user == null) {
            return Result.error("该手机号尚未注册，请联系管理员");
        }
        return Result.success(user);
    }

    /**
     * 用户注册接口
     * POST /api/v1/user/register
     */
    @PostMapping("/register")
    public Result<SysUser> register(@RequestBody SysUser user) {
        // 1. 检查手机号是否已存在
        SysUser existing = userService.getByPhone(user.getPhone());
        if (existing != null) {
            return Result.error("该手机号已注册，请直接登录");
        }

        // 2. 初始化默认值
        user.setRiskScore(0);
        user.setRiskLevel(0);
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            user.setUsername("新用户" + user.getPhone().substring(7));
        }

        // 3. 保存
        userService.save(user);
        return Result.success(user);
    }


    /**
     * 绑定子女手机号接口
     * POST /api/v1/user/bind
     */
    @PostMapping("/bind")
    public Result<String> bindFamily(@RequestBody BindRequest request) {
        if (request.getElderId() == null || request.getChildPhone() == null) {
            return Result.error("参数不完整");
        }
        boolean success = userService.bindGuardian(request.getElderId(), request.getChildPhone());
        return success ? Result.success("关联成功") : Result.error("关联失败");
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    public Result<SysUser> getInfo(@PathVariable Long id) {
        SysUser user = userService.getById(id);
        return user != null ? Result.success(user) : Result.error("用户不存在");
    }
}

/**
 * 登录请求对象
 */
@Data
class LoginRequest {
    private String phone;
}

/**
 * 绑定请求对象
 */
@Data
class BindRequest {
    private Long elderId;
    private String childPhone;
}