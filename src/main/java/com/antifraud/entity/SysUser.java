package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    private String phone;
    private String password;
    private Integer age;
    private String avatar;

    /**
     * 关联子女的手机号
     * 用于接收系统的风险预警短信或应用内推送
     */
    private String guardianPhone;

    private Integer riskScore; // 0-100
    private Integer riskLevel; // 0:低, 1:中, 2:高
    private String role; // USER, ADMIN

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
