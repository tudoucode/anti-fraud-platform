package com.antifraud.service;

import com.antifraud.entity.SysUser;
import com.antifraud.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    /**
     * 根据手机号查询老人信息
     */
    public SysUser getByPhone(String phone) {
        return this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, phone));
    }

    /**
     * 绑定或修改子女手机号
     * @param elderId 老人ID
     * @param childPhone 子女手机号
     * @return 是否成功
     */
    public boolean bindGuardian(Long elderId, String childPhone) {
        SysUser user = this.getById(elderId);
        if (user != null) {
            user.setGuardianPhone(childPhone);
            return this.updateById(user);
        }
        return false;
    }
}