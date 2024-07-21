package com.ming.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.entities.SysUser;
import com.ming.entities.query.AppletLoginQuery;
import com.ming.mapper.SysUserMapper;
import com.ming.util.http.Result;

import java.util.List;
import java.util.Map;

/**
 * @Author liming
 * @Date 2023/3/31 11:19
 */
public interface SysUserService extends IService<SysUser>,IPolymorphicService {
    IPage<SysUser> userPage(Integer pageNo, Integer pageSize);

    IPage<SysUser> userPage2(Integer pageNo, Integer pageSize);

    void login(String username, String password);

    int updateUserById(SysUser sysUser);

    /**
     * 校验用户
     * @param appletLoginQuery
     * @return
     */
    boolean isItaVillage(AppletLoginQuery appletLoginQuery);

    boolean saveUser(SysUser sysUser);


    List<Map<String, Object>> infoList();
}
