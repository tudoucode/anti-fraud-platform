package com.antifraud.mapper;

import com.antifraud.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    // MyBatis Plus 会自动实现基础 CRUD，无需写 XML
}
