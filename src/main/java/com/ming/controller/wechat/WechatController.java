package com.ming.controller.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ming.entities.query.AppletLoginQuery;
import com.ming.service.IWechatService;
import com.ming.service.SysUserService;
import com.ming.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author liming
 * @Date 2023/6/16 14:15
 */
@Api(tags = "小程序")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wechat")
public class WechatController {


    private final IWechatService iWechatService;
    private final SysUserService sysUserService;

    /**
     * 通过APPID、APPSECRET获取小程序token
     * 有效期7200秒
     * @return
     */
    @ApiOperation("通过微信code获取微信用户信息")
    @GetMapping(value = "/getToken")
    public Result getToken() {
        String token = iWechatService.getToken();
        return Result.success(token);
    }



    /**
     * 通过微信code获取登录信息
     *
     * @param appletLoginQuery
     * @return
     */
    @ApiOperation("通过微信code获取登录信息")
    @PostMapping(value = "/codeGetUser")
    public Result codeGetUser(@RequestBody AppletLoginQuery appletLoginQuery) {
        // 判断用户名密码是否合法
        if(sysUserService.isItaVillage(appletLoginQuery)){
            return Result.failure(2,"用户名密码错误");
        };
        return iWechatService.codeGetUser(appletLoginQuery);
    }


}
