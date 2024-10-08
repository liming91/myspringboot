package com.ming.controller;

import com.ming.annotation.ResultAnnotation;
import com.ming.bean.Test;
import com.ming.exception.Asserts;
import com.ming.service.IAsyncService;
import com.ming.util.http.Result;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HelloController {
    private static Logger log = LoggerFactory.getLogger(HelloController.class);

    private final JdbcTemplate jdbcTemplate;

    private final IAsyncService iAsyncService;

    /**
     * 首页 模板引擎
     *
     * @return
     */
    @RequestMapping({"/","/login.html"})
    public String index(){
        return "login";
    }


    @ResponseBody
    @RequestMapping("/hello")
    public Result<?> hello(@RequestParam("user") String user) throws IOException {
        if (user.equals("aaa")) {
            Asserts.fail("错误账户");
        }
        Map<String, Object> map = new HashMap<>();
        String staticPath = this.getClass().getClassLoader().getResource("images").getFile();
        log.info("staticPath:{}", staticPath);
        File file = new File("src/main/resources/images/logo.jpg");
        String canonicalPath = file.getCanonicalPath();
        map.put("hello", canonicalPath);
        return Result.success(map);
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
    @ResultAnnotation
    public List<Test> findEmp() {
        //List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from test ");
        List<Test> list = iAsyncService.getTest();
        return list;
    }


}
