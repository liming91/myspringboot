package com.ming.controller;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.ming.exception.UserNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class HelloController {

    @Autowired
    JdbcTemplate jdbcTemplate;

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
    public String hello(@RequestParam("user") String  user) {
        if(user.equals("aaa")){
            throw new UserNotExistException();
        }
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

    @ResponseBody
    @GetMapping("/query")
    public Map<String,Object> findEmp(){
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from department ");
        return list.get(0);
    }


}
