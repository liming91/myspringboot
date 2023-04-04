package com.ming.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.entities.SysUser;
import org.springframework.scheduling.annotation.Async;

/**
 * @Author liming
 * @Date 2023/4/4 14:50
 */
public interface IUserCache {

    IPage<SysUser> getCachePage(final String key, Page<SysUser> page, String keywords);


    /**
     * 根据id修改用户缓存
     * @param userId
     * @return
     */
    boolean updateCacheByUserId(String userId,SysUser sysUser);

    /**
     * 根据id删除用户缓存
     * @param userId
     * @return
     */
    void delCacheByUserId(String userId);

    /**
     * 刷新缓存
     *
     * @param key
     */
    void refreshCache(final String key);

    /**
     * 异步刷新缓存
     *
     * @param key
     */
    @Async
    void asyncRefreshCache(final String key);

    /**
     * 删除缓存
     *
     * @param key
     */
    void removeCache(final String key);



}
