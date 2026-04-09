package com.antifraud.controller;
import com.antifraud.common.Result;
import com.antifraud.entity.KnowledgeItem;
import com.antifraud.mapper.KnowledgeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/knowledge")
@CrossOrigin
public class KnowledgeController {
    @Autowired
    private KnowledgeMapper knowledgeMapper;

    @GetMapping("/list")
    public Result<List<KnowledgeItem>> getList() {
        return Result.success(knowledgeMapper.selectList(null));
    }
}
