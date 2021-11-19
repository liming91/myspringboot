package com.ming.controller;

import com.google.common.collect.Lists;
import com.ming.bean.Test;
import com.ming.mapper.TestMapper;
import com.ming.service.IAsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

@RestController
public class TestController {
    @Autowired
    private Executor threadPoolTaskExecutor; // 注入线程池
    @Autowired
    private TestMapper testMapper;
    @Autowired
    private IAsyncService iAsyncService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/test")
    public String test() {
        long startTime = System.currentTimeMillis();

        List<Test> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Test test = new Test();
            test.setId(String.valueOf(i));
            test.setName("name" + i);
            list.add(test);
        }
        // 注入线程池
        ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(
                threadPoolTaskExecutor);
        List<List<Test>> lists = Lists.partition(list, 5000);
        lists.forEach(item -> {
            // 这里做的事情就是 根据lists大小确认要多少个线程 给每个线程分配任务
            completionService.submit(new Callable() {
                @Override
                public Object call() throws Exception {
                    // insertList()方法 就是批量insert语句
                    return testMapper.addTest(item);
                }
            });
        });
        // 这里是让多线程开始执行
        lists.forEach(item -> {
            try {
                completionService.take().get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e);
            }
        });
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        logger.info("耗时：" + time);
        return "ok";
    }


    @GetMapping("/test2")
    public String test2() {
        List<Test> list = new ArrayList<>();
        for (int i = 0; i < 200000; i++) {
            Test test = new Test();
            test.setId(String.valueOf(i));
            test.setName("name" + i);
            list.add(test);
        }
        iAsyncService.test2(list);
        return "ok";
    }
}
