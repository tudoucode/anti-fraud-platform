package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("fraud_case")
public class FraudCase {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;         // 案例标题
    private String category;      // 分类: HEALTH(保健), FINANCE(理财), GOV(公检法), FAMILY(亲情)
    private String summary;       // 简介
    private String content;       // 详细内容
    private String videoUrl;      // 视频链接
    private Integer viewCount;    // 浏览量
    private Boolean isPublished;  // 是否发布
    private LocalDateTime createTime; // 创建时间
}
