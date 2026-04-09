package com.antifraud.mapper;

import com.antifraud.entity.FraudCase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FraudCaseMapper extends BaseMapper<FraudCase> {
}
