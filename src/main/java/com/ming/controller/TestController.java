package com.ming.controller;

import com.ming.bean.Test;
import com.ming.mapper.TestMapper;
import com.ming.service.IAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;

@RestController
public class TestController {
    @Autowired
    private IAsyncService iAsyncService;

    @GetMapping("/test")
    public String test() {
        List<Test> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Test test = new Test();
            test.setId(String.valueOf(i));
            test.setName("name"+i);
            list.add(test);
        }
        iAsyncService.executeAsync(list);
        return "ok";
    }
}
