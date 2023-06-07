package com.ming.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.annotation.DateVersion;
import com.ming.entities.SysUser;
import com.ming.enums.ResultCode;
import com.ming.service.SysUserService;
import com.ming.util.RedisLockUtil;
import com.ming.util.http.ResponseResult;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Facet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final RedisLockUtil redisLockUtil;

    private final SysUserService sysUserService;

    private final static String KEY = "user:" + System.currentTimeMillis();

    @ApiOperation("用户列表")
    @GetMapping("/userPage")
    public ResponseResult<?> userPage(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<SysUser> list = sysUserService.userPage(pageNo, pageSize);
        return ResponseResult.success(list);
    }

    @ApiOperation("用户列表")
    @GetMapping("/userPage2")
    public ResponseResult<?> userPage2(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<SysUser> list = sysUserService.userPage2(pageNo, pageSize);
        return ResponseResult.success(list);
    }

    @ApiOperation("用户列表")
    @PostMapping("/save")
    public ResponseResult<?> save(@RequestBody SysUser sysUser) {
        boolean flag = false;
        if (redisLockUtil.tryLock(KEY, 100)) {
            log.info("测试：加锁成功！");
            flag = sysUserService.save(sysUser);
            redisLockUtil.unLock(KEY);
            log.info("测试：释放锁成功！");
        } else {
            System.out.println("测试：加锁失败！");
        }
        if (flag) {
            return ResponseResult.success(ResultCode.E02);
        }
        return ResponseResult.success(ResultCode.E03);

    }


    @ApiOperation("用户列表")
    @PostMapping("/update1")
    @DateVersion(tableName = "sys_user", idName = "userId")
    public ResponseResult<?> update1(@RequestBody SysUser sysUser) {
        int rows = sysUserService.updateUserById(sysUser);
        redisLockUtil.unLock(KEY);
        if (rows > 0) {
            return ResponseResult.success(ResultCode.E02);
        }
        return ResponseResult.failure(ResultCode.E03);

    }

    @ApiOperation("用户列表")
    @PostMapping("/update")
    public ResponseResult<?> update(@RequestBody SysUser sysUser) {
        int flag = 0;
        if (redisLockUtil.tryLock(KEY, 100)) {
            log.info("测试：加锁成功！");
            flag = sysUserService.updateUserById(sysUser);
            redisLockUtil.unLock(KEY);
            log.info("测试：释放锁成功！");
        } else {
            System.out.println("测试：加锁失败！");
        }
        if (flag > 0) {
            return ResponseResult.success(ResultCode.E02);
        }
        return ResponseResult.failure(ResultCode.E03);

    }
}
