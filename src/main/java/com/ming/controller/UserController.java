package com.ming.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.bean.GenerateResult;
import com.ming.bean.Result;
import com.ming.entities.SysUser;
import com.ming.service.SysUserService;
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
    public Result<?> userPage(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<SysUser> list = sysUserService.userPage(pageNo, pageSize);
        return GenerateResult.genDataSuccessResult(list);
    }

    @ApiOperation("用户列表")
    @GetMapping("/userPage2")
    public Result<?> userPage2(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<SysUser> list = sysUserService.userPage2(pageNo, pageSize);
        return GenerateResult.genDataSuccessResult(list);
    }

    @ApiOperation("用户列表")
    @PostMapping("/save")
    public void  save(@RequestBody SysUser sysUser){
        for (int i = 1; i <= 100000; i++) {
            sysUser.setUserName(i+"");
            sysUser.setPhonenumber(i+"");
            sysUserService.save(sysUser);
        }

    }
}
