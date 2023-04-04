package com.ming.service.impl;

import cn.hutool.core.date.StopWatch;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.constant.Constants;
import com.ming.entities.SysUser;
import com.ming.mapper.SysUserMapper;
import com.ming.service.IUserCache;
import com.ming.util.RedisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author liming
 * @Date 2023/4/4 14:52
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserCacheImpl implements IUserCache {
    private final RedisService redisService;
    private final SysUserMapper sysUserMapper;
    @Override
    public IPage<SysUser> getCachePage(String key, Page<SysUser> page, String keywords) {
        Map<Object, Object> userData = redisService.hmget(Constants.CACHE_USERS);
        if(userData.size() > 0){
            dataEncapsulation(page,mapToBeanList(userData),keywords);
            return page;
        }
        IPage<SysUser> sysUserIPage = sysUserMapper.userPage(page, keywords);
        this.asyncRefreshCache(key);
        return sysUserIPage;
    }


    @Override
    public boolean updateCacheByUserId(String userId, SysUser sysUser) {
        Map<String,Object> map = new HashMap<>();
        String key = Constants.CACHE_USER_ID + userId;
        map.put(key,sysUser);
        return redisService.hmset(Constants.CACHE_USERS,map);
    }

    @Override
    public void delCacheByUserId(String userId) {
        redisService.hdel(Constants.CACHE_USERS,Constants.CACHE_USER_ID + userId);
    }

    @Async
    @Override
    public void asyncRefreshCache(String key) {
        refreshCache(key);
    }


    @Override
    public void removeCache(String key) {
        redisService.del(key);
    }

    @Override
    public void refreshCache(String key) {
        log.info("用户列表刷新缓存开始...");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<SysUser> ybBaseUserInfoDepartDTOS = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>().eq(SysUser::getStatus,0));
        Map<String,Object> map = new HashMap<>();
        ybBaseUserInfoDepartDTOS.stream().forEach(u ->{
            String itemKey = Constants.CACHE_USER_ID + u.getUserId();
            map.put(itemKey,u);
        });
        removeCache(key);
        redisService.hmset(key,map);
        stopWatch.stop();
        log.info("用户列表刷新缓存完成,耗时:{}秒",stopWatch.getTotalTimeSeconds());
    }

    public Boolean ifContains(String string, String str){
        if(StringUtils.isBlank(string)){
            return false;
        }
        return string.contains(str);
    }

    private void dataEncapsulation(Page<SysUser> page, List<SysUser> objectToBean, String keywords) {
        if(StringUtils.isNotBlank(keywords)){
            objectToBean = objectToBean.stream()
                    .filter(ob -> ifContains(ob.getUserName(),keywords)
                            || ifContains(ob.getPhonenumber(),keywords)
                            || ifContains(ob.getEmail(),keywords))
                    .collect(Collectors.toList());
        }
        if(CollectionUtils.isEmpty(objectToBean)){
            page.setTotal(0);
            page.setPages(0);
            page.setRecords(new ArrayList<>());
        }else {
            //objectToBean.sort(Comparator.comparing(SysUser::getLoginDate).reversed());
            List<SysUser> records = objectToBean.stream().skip((page.getCurrent() - 1) * page.getSize())
                    .limit(page.getSize()).collect(Collectors.toList());
            page.setTotal(objectToBean.size());
            page.setPages(objectToBean.size() / page.getSize());
            page.setRecords(records);
        }
    }


    public static List<SysUser> mapToBeanList(Map<Object,Object> map){
        List<SysUser> users = new ArrayList<>();
        for (Map.Entry<Object, Object> objectObjectEntry : map.entrySet()) {
            SysUser user = JSON.parseObject(JSON.toJSONString(objectObjectEntry.getValue()), SysUser.class);
            users.add(user);
        }
        return users;
    }
}
