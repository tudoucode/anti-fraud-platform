package com.antifraud.dto;
import lombok.Data;

@Data
public class LoginDto {
    private String phone;
    private String code; // 验证码 (预留)
}