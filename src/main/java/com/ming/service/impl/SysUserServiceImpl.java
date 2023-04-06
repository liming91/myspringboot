package com.ming.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.bean.MessageEnum;
import com.ming.constant.Constants;
import com.ming.entities.SysUser;
import com.ming.exception.ServiceException;
import com.ming.manager.AsyncManager;
import com.ming.manager.factory.AsyncFactory;
import com.ming.mapper.SysUserMapper;
import com.ming.service.IUserCache;
import com.ming.service.SysUserService;
import com.ming.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author liming
 * @Date 2023/3/31 11:22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final IUserCache iUserCache;

    private final RedisTemplate redisTemplate;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;


    @Override
    public IPage<SysUser> userPage(Integer pageNo, Integer pageSize) {
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        //sysUserMapper.userPage(page, null);
        return iUserCache.getCachePage(Constants.CACHE_USERS, page, null);
    }

    @Override
    public IPage<SysUser> userPage2(Integer pageNo, Integer pageSize) {
        List<SysUser> list = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>().isNotNull(SysUser::getUserName));
        List<SysUser> collect = list.stream().skip((pageNo - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        Page<SysUser> page = new Page(pageNo, pageSize);
        page.setRecords(collect);
        page.setSize(pageSize);
        page.setTotal(list.size());
        return page;
    }


    @Override
    public void login(String username, String password) {
        if (StringUtils.isNull(username)) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException("登录用户：" + username + " 不存在");
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setPassword(password);
        validate(sysUser);
    }

    public void validate(SysUser user) {

        passwordValidate(user);
    }

    /**
     * 密码校验
     * @param user
     */
    private void passwordValidate(SysUser user) {
        Integer retryCount = (Integer) redisTemplate.boundValueOps(getCacheKey(user.getUserName())).get();
        if(ObjectUtil.isEmpty(retryCount)){
            retryCount=0;
        }

        if (retryCount >= Integer.valueOf(maxRetryCount).intValue()) {

            throw new ServiceException("密码连续输入错误,错误5次以上");
        }

        if(!user.getPassword().equals("724933")){
            retryCount = retryCount + 1;
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getUserName(), Constants.LOGIN_FAIL,
                   "密码重试次数", retryCount));
            redisTemplate.opsForValue().set(getCacheKey(user.getUserName()),retryCount,lockTime,TimeUnit.MINUTES);
            throw new ServiceException("密码错误，错误次数:"+retryCount);
        }
    }

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username)
    {
        return Constants.PWD_ERR_CNT_KEY + username;
    }
}
