package com.ming.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.constant.Constants;
import com.ming.entities.SysUser;
import com.ming.entities.query.AppletLoginQuery;
import com.ming.exception.ServiceException;
import com.ming.manager.AsyncManager;
import com.ming.manager.factory.AsyncFactory;
import com.ming.mapper.SysUserMapper;
import com.ming.service.IUserCache;
import com.ming.service.SysUserService;
import com.ming.util.RedisLockUtil;
import com.ming.util.StringUtils;
import com.ming.util.http.Result;
import com.ming.util.redis.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private RedisLockUtil redisLockUtil;

    @Autowired
    private RedisCache redisCache;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;
    String redisLockKey = String.format("%s:lock:%s", "USER_", "LOCK_NAME");

    /**
     * 3*24*60*60
     * 3天提醒
     */
    private long passwordExpireTip = 1 * 60;


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

    private void passwordExpire(String username, String password) {
        List<Object> cacheList = redisCache.getCacheList("password:list:" + username);
        if (StringUtils.isEmpty(cacheList)) {
            redisCache.setCacheObject("password:expire:" + username, password, 3, TimeUnit.MINUTES);
            List<String> list = new LinkedList<String>();
            list.add(password);
            redisCache.setCacheList("password:list:" + username, list);
        }
    }


    public void pwdReminder(String username) {
        Map<String, Object> resMap = new HashMap<>();
        boolean isRestPwd = false;
        Long expire = redisTemplate.getExpire("password:expire:" + username);
        if (expire < passwordExpireTip) {
            long days = TimeUnit.SECONDS.toDays(expire);
            long hours = TimeUnit.SECONDS.toHours(expire) - TimeUnit.DAYS.toHours(days);
            isRestPwd = true;
            resMap.put("timeTip", "密码还有" + days + "天" + hours + "小时到期，点击确定修改密码！");
            log.info(expire + "秒到期，点击确定修改密码！");
        }
        resMap.put("isRestPwd", isRestPwd);

    }

    @Override
    public void login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException(String.format("登录用户：%s不存在", username));

        }
        passwordExpire(username, password);
        String redisPassword = redisCache.getCacheObject("password:expire:" + username);
        if (StringUtils.isEmpty(redisPassword)) {
            throw new ServiceException("密码已过期，请联系管理员修改！");
        }


        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setPassword(password);
        validate(sysUser);

        pwdReminder(username);
    }

    public void validate(SysUser user) {

        passwordValidate(user);
    }

    /**
     * 密码校验
     *
     * @param user
     */
    private void passwordValidate(SysUser user) {
        Integer retryCount = (Integer) redisTemplate.boundValueOps(getCacheKey(user.getUserName())).get();
        if (ObjectUtil.isEmpty(retryCount)) {
            retryCount = 0;
        }

        if (retryCount >= Integer.valueOf(maxRetryCount).intValue()) {

            throw new ServiceException("密码连续输入错误,错误5次以上");
        }

        if (!user.getPassword().equals("123456")) {
            retryCount = retryCount + 1;
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getUserName(), Constants.LOGIN_FAIL,
                    "密码重试次数", retryCount));
            redisTemplate.opsForValue().set(getCacheKey(user.getUserName()), retryCount, lockTime, TimeUnit.MINUTES);
            throw new ServiceException("密码错误，错误次数:" + retryCount);
        }
    }

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {
        return Constants.PWD_ERR_CNT_KEY + username;
    }


    @Override
    public int updateUserById(SysUser sysUser) {
        return sysUserMapper.updateUserById(sysUser);
    }


    @Override
    public boolean isItaVillage(AppletLoginQuery appletLoginQuery) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, appletLoginQuery.getUserJobNo())
                .eq(SysUser::getPassword, appletLoginQuery.getPwd());
        List<SysUser> sysUsers = this.baseMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(sysUsers)) {
            return true;
        }
        return false;
    }


    @Override
    public boolean saveUser(SysUser sysUser) {
        //1：加锁 2：查询有没有重复数据 3：更新
        try {
            if (redisLockUtil.tryLock(redisLockKey, sysUser.getUserName(), 30)) {
                log.info("获取redis锁 [" + redisLockKey + "] 成功,执行以下业务逻辑!");
                //查询重复数据
                LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, sysUser.getUserName());
                List<SysUser> sysUsers = this.baseMapper.selectList(queryWrapper);
                if (CollectionUtils.isNotEmpty(sysUsers)) {
                    return this.save(sysUser);
                }
            } else {
                log.info("获取redis锁 [" + redisLockKey + "] 失败,锁占用!");
                throw new ServiceException("重复操作!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            boolean releaseLock = redisLockUtil.unLock(redisLockKey);
            if (releaseLock) {
                log.info("释放redis锁 [" + redisLockKey + "] 成功!");
            } else {
                log.info("释放redis锁 [" + redisLockKey + "] 成功!");
            }
        }
        return false;
    }


    @Override
    public List<Map<String, Object>> infoList() {

        return this.baseMapper.getUserMap();
    }
}
