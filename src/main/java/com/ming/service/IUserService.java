package com.ming.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.entities.SysUser;

/**
 * @Description
 * @Author liming
 * @Date 2025/4/30 10:58
 */
public interface IUserService extends IService<SysUser> {

    void lockUser();
}
