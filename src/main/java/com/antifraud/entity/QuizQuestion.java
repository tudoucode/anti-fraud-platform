package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("quiz_question")
public class QuizQuestion {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String content;       // 题目内容
    private String optionA;      // 选项A（通常是迷惑项）
    private String optionB;      // 选项B（通常是正确项）
    private String correctOption; // 正确答案标识: A 或 B
    private String analysis;     // 答题后的解析说明
}