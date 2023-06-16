package com.ming.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ming.constant.WechatConfiguration;
import com.ming.entities.query.AppletLoginQuery;
import com.ming.service.IWechatService;
import com.ming.util.http.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author liming
 * @Date 2023/6/16 14:44
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatServiceImpl implements IWechatService {


    @Override
    public String getToken() {
        String url = WechatConfiguration.getAccessToken + "?grant_type=client_credential" +
                "&appid=" + WechatConfiguration.AppID +
                "&secret=" + WechatConfiguration.AppSecret;
        String result = HttpRequest.get(url)
                .timeout(20000)
                .execute()
                .charset("UTF-8")
                .body();
        JSONObject object = JSON.parseObject(result);
        return String.valueOf(object.get("access_token"));
    }


    @Override
    public Result codeGetUser(AppletLoginQuery appletLoginQuery) {
        //wx.login是小程序的一个API库，可以直接利用js代码调用，每一次获取到的code都是不同的，因为他只是一个登录凭证，
        String code = appletLoginQuery.getCode();
        //请求微信校验接口
        String url = StrUtil.format(WechatConfiguration.jsCode2session
                        + "?appid={}&secret={}&js_code={}&grant_type=authorization_code"
                , WechatConfiguration.AppID, WechatConfiguration.AppSecret, code);

        String result = HttpRequest.get(url)
                .timeout(20000)
                .execute()
                .charset("UTF-8")
                .body();
        JSONObject object = JSON.parseObject(result);
        return Result.success(object);
    }
}
