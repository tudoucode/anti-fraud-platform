package com.antifraud.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("knowledge_item")
public class KnowledgeItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String type; // VIDEO, TIP
    private String content;
    private String duration;
    private String icon;
}
