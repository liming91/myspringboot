package com.ming.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.entities.SysUser;
import com.ming.mapper.SysUserMapper;

/**
 * @Author liming
 * @Date 2023/3/31 11:19
 */
public interface SysUserService extends IService<SysUser> {
    IPage<SysUser> userPage(Integer pageNo, Integer pageSize);

    IPage<SysUser> userPage2(Integer pageNo, Integer pageSize);

    void login(String username, String password);

    int updateUserById(SysUser sysUser);
}
