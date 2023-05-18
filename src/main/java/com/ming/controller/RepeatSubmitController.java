package com.ming.controller;

import com.ming.annotation.ForbidRepeatSubmit;
import com.ming.entities.SysUser;
import com.ming.enums.ResultCode;
import com.ming.enums.SubmitEnum;
import com.ming.service.SysUserService;
import com.ming.util.http.ResponseResult;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



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
    @ForbidRepeatSubmit(type = SubmitEnum.IP, time = 3 * 60)// IP方式 三分钟内不能重复提交
    public ResponseResult<?> add(@RequestBody SysUser sysUser) {
        boolean flag = sysUserService.save(sysUser);
        if(flag){
            return ResponseResult.success(ResultCode.E02);
        }
        return ResponseResult.success(ResultCode.E03);
    }

}
