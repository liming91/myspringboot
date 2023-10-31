package com.ming.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.annotation.DateVersion;
import com.ming.entities.SysUser;
import com.ming.enums.ResultCode;
import com.ming.service.SysUserService;
import com.ming.util.RedisLockUtil;
import com.ming.util.http.Result;
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
    public Result<?> userPage(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<SysUser> list = sysUserService.userPage(pageNo, pageSize);
        return Result.success(list);
    }

    @ApiOperation("用户列表")
    @GetMapping("/userPage2")
    public Result<?> userPage2(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<SysUser> list = sysUserService.userPage2(pageNo, pageSize);
        return Result.success(list);
    }

    @ApiOperation("添加用户")
    @PostMapping("/save")
    public Result<?> save(@RequestBody SysUser sysUser) {
        boolean flag = sysUserService.save(sysUser);
        if (flag) {
            return Result.success(ResultCode.E02);
        }
        return Result.success(ResultCode.E03);
    }

    @ApiOperation("修改用户1")
    @PostMapping("/update1")
    @DateVersion(tableName = "sys_user", idName = "userId")
    public Result<?> update1(@RequestBody SysUser sysUser) {
        int rows = sysUserService.updateUserById(sysUser);
        if (rows > 0) {
            return Result.success(ResultCode.E02);
        }
        return Result.failure(ResultCode.E03);
    }

    @ApiOperation("修改用户")
    @PostMapping("/update")
    public Result<?> update(@RequestBody SysUser sysUser) {
        int flag = sysUserService.updateUserById(sysUser);
        if (flag > 0) {
            return Result.success(ResultCode.E02);
        }
        return Result.failure(ResultCode.E03);
    }



}
