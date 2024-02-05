package com.ming.controller;

import com.ming.annotation.ForbidRepeatSubmit;
import com.ming.annotation.RepeatSubmit;
import com.ming.entities.SysUser;
import com.ming.enums.ResultCode;
import com.ming.enums.SubmitEnum;
import com.ming.service.SysUserService;
import com.ming.util.http.Result;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * 防重提交
 *
 * @Author liming
 * @Date 2023/5/18 15:46
 */
@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "防重提交")
@RequestMapping("/repeat")
public class RepeatSubmitController {

    private final SysUserService sysUserService;

    @PostMapping("/save")
    //@RepeatSubmit(interval = 30000, message = "3秒不允许重复操作")
    public Result<?> save(@RequestBody SysUser sysUser) {
        boolean flag = sysUserService.saveUser(sysUser);
        if (flag) {
            return Result.success(ResultCode.E02);
        }
        return Result.success(ResultCode.E03);
    }


    @PostMapping("/add")
    @ForbidRepeatSubmit(type = SubmitEnum.IP, time = 3 * 60)// IP方式 三分钟内不能重复提交
    public Result<?> add(@RequestBody SysUser sysUser) {
        boolean flag = sysUserService.save(sysUser);
        if (flag) {
            return Result.success(ResultCode.E02);
        }
        return Result.success(ResultCode.E03);
    }


    @DeleteMapping
    @ForbidRepeatSubmit(type = SubmitEnum.TOKEN, time = 3 * 60)// TOKEN方式 三分钟内不能重复提交
    public Result<?> remove(@RequestBody Long userId) {

        boolean flag = sysUserService.removeById(userId);
        if (flag) {
            return Result.success();
        } else {
            return Result.failure(ResultCode.E03);
        }
    }


}
