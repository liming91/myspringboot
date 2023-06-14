package com.ming.controller;

import com.ming.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginController {
    Logger log = LoggerFactory.getLogger(getClass());
    private final SysUserService sysUserService;
    private final static String PASSWORD = "123456";

    //@RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @PostMapping("/user/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String, Object> map,
                        HttpSession session) {
        sysUserService.login(username, password);
        if (!StringUtils.isEmpty(username) && password.equals(PASSWORD)) {
            //登陆成功,为了防止表单重复提交，可以重定向到主页
            //配置registry.addViewController("/main.html").setViewName("dashboard");
            session.setAttribute("loginUser", username);
            return "redirect:/main.html";
        } else {
            //登陆失败,来到登录页面显示错误消息
            map.put("msg", "用户名密码错误");
            return "login";
        }

    }


}
