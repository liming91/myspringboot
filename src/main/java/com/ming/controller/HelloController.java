package com.ming.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class HelloController {

    /**
     * 首页 模板引擎
     * @return
     */
//    @RequestMapping({"/","/index.html"})
//    public String index(){
//        return "index";
//    }

    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        return "Hello word";
    }

    /**
     * thymeleaf测试
     *
     * @return
     */
    @RequestMapping("/success")
    public String Success(Map<String, Object> map) {
        //"classpath:/templates/success.html
        map.put("hello", "你好！");
        return "success";
    }
}
