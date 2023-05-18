package com.ming.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.bean.GenerateResult;
import com.ming.bean.MessageEnum;
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
    public Result<?>  save(@RequestBody SysUser sysUser){
        boolean flag = sysUserService.save(sysUser);
        if(flag){
            return GenerateResult.genSuccessResult(MessageEnum.E00);
        }
        return GenerateResult.genSuccessResult(MessageEnum.E01);

    }

    @ApiOperation("用户列表")
    @PostMapping("/update")
    public Result<?>  update(@RequestBody SysUser sysUser){

        int rows = sysUserService.updateUserById(sysUser);
        if(rows>0){
            return GenerateResult.genSuccessResult(MessageEnum.E00);
        }
        return GenerateResult.genSuccessResult(MessageEnum.E01);

    }
}
