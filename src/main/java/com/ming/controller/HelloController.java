package com.ming.controller;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.ming.annotation.ResultAnnotation;
import com.ming.bean.GenerateResult;
import com.ming.bean.MessageEnum;
import com.ming.bean.Result;
import com.ming.bean.Test;
import com.ming.enums.ResultCode;
import com.ming.exception.UserNotExistException;
import com.ming.service.IAsyncService;
import com.ming.util.http.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Objects;

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
//    @RequestMapping({"/","/index.html"})
//    public String index(){
//        return "index";
//    }
    @ResponseBody
    @RequestMapping("/hello")
    public Result<?> hello(@RequestParam("user") String user) throws IOException {
        if (user.equals("aaa")) {
            throw new UserNotExistException();
        }
        Map<String, Object> map = new HashMap<>();
        String staticPath = this.getClass().getClassLoader().getResource("image").getFile();
        log.info("staticPath:{}", staticPath);
        File file = new File("src/main/resources/image/log.jpg");
        String canonicalPath = file.getCanonicalPath();
        map.put("hello", canonicalPath);
        //GenerateResult.genSuccessResult(MessageEnum.E00);
        return GenerateResult.genDataSuccessResult(map);
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
