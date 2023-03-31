package com.ming.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.bean.GenerateResult;
import com.ming.bean.Result;
import com.ming.entities.SysUser;
import com.ming.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
