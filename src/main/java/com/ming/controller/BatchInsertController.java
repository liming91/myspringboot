package com.ming.controller;

import com.google.common.collect.Lists;
import com.ming.bean.GenerateResult;
import com.ming.bean.MessageEnum;
import com.ming.bean.Result;
import com.ming.bean.Test;
import com.ming.entities.HbBaseEnterUser;
import com.ming.mapper.TestMapper;
import com.ming.service.IAsyncService;
import com.ming.service.IHbBaseEnterUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

/**
 * 批量插入百万数据
 */
@RestController
public class BatchInsertController {
    @Autowired
    private Executor threadPoolTaskExecutor; // 注入线程池
    @Autowired
    private TestMapper testMapper;
    @Autowired
    private IAsyncService iAsyncService;
    @Autowired
    private IHbBaseEnterUserService iHbBaseEnterUserService;

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

    //数据分隔
    private static final Integer splitSize = 1000;

    public <T> List<List<T>> subList(List<T> list) {
        List<List<T>> lists = new ArrayList<>();
        int size = list.size();
        if (size <= splitSize) {
            lists.add(list);
            return lists;
        }
        int number = size / splitSize;
        //完整的分隔部分
        for (int i = 0; i < number; i++) {
            int startIndex = i * splitSize;
            int endIndex = (i + 1) * splitSize;
            lists.add(list.subList(startIndex, endIndex));
        }
        //最后分隔剩下的部分直接放入list
        if (number * splitSize == size) {
            return lists;
        }
        lists.add(list.subList(number * splitSize, size));
        return lists;
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


    @GetMapping("/test3")
    public String test3() {
        List<Test> list = new ArrayList<>();
        for (int i = 0; i < 200000; i++) {
            Test test = new Test();
            test.setId(String.valueOf(i));
            test.setName("name" + i);
            list.add(test);
        }
        iAsyncService.test3(list);
        return "ok";
    }

    /**
     * 批量插入
     *
     * @param
     * @return
     */
    @PostMapping("/batchUser")
    public Result<?> addGrantEnter(@RequestBody List<HbBaseEnterUser> hbBaseEnterUser) {
        if (StringUtils.isEmpty(hbBaseEnterUser)) {
            return GenerateResult.genSuccessResult(MessageEnum.E01);
        }
        int flag = iHbBaseEnterUserService.addGrantEnter(hbBaseEnterUser);
        if (flag != 0) {
            return GenerateResult.genSuccessResult(MessageEnum.E00);
        }
        return GenerateResult.genSuccessResult(MessageEnum.E01);
    }


    @GetMapping("/getTest")
    public Result<?> getTest() {
        List<Test> list = iAsyncService.getTest();
        return GenerateResult.genDataSuccessResult(list);
    }
}
