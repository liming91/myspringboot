package com.ming.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ming.bean.GenerateResult;
import com.ming.bean.MessageEnum;
import com.ming.bean.Result;
import com.ming.bean.Test;
import com.ming.entities.HbBaseEnterUser;
import com.ming.mapper.TestMapper;
import com.ming.service.IAsyncService;
import com.ming.service.IHbBaseEnterUserService;
import com.ming.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.SecureRandom;
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
    private IAsyncService iAsyncService;
    @Autowired
    private IHbBaseEnterUserService iHbBaseEnterUserService;

    @Autowired
    private RedisUtil redisUtil;

    public static final int START = 1;
    //定义范围开始数字
    public static final int END = 100;
    //定义范围结束数字
    SecureRandom random = new SecureRandom();

    Logger logger = LoggerFactory.getLogger(getClass());


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
     * 批量插入百万数据测试
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
        Object obj = redisUtil.get("test");
        List<Test> list;
        if (obj == null) {
            list = iAsyncService.getTest();
            String jsonString = JSON.toJSONString(list);
            // 为防止缓存雪崩 缓存时间1200 + 随机数
            // 1200 + random.nextInt(END - START + 1) + START
            redisUtil.set("test", jsonString, 60);
            return GenerateResult.genDataSuccessResult(list);
        } else {
            list = JSON.parseArray((String) obj, Test.class);
            logger.info("从缓存取==Test：{}", list);
        }
        return GenerateResult.genDataSuccessResult(list);

    }
}
