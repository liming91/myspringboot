package com.ming.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.entities.SysUser;
import com.ming.enums.ResultCode;
import com.ming.service.SysUserService;
import com.ming.util.http.ResponseResult;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


    private final SysUserService sysUserService;


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
        boolean flag = sysUserService.save(sysUser);
        if (flag) {
            return ResponseResult.success(ResultCode.E02);
        }
        return ResponseResult.success(ResultCode.E03);

    }

    @ApiOperation("用户列表")
    @PostMapping("/update")
    public ResponseResult<?> update(@RequestBody SysUser sysUser) {

        int rows = sysUserService.updateUserById(sysUser);
        if (rows > 0) {
            return ResponseResult.success(ResultCode.E02);
        }
        return ResponseResult.success(ResultCode.E03);

    }
}
