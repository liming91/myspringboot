package com.ming.service.impl;

import com.ming.bean.Test;
import com.ming.mapper.TestMapper;
import com.ming.service.IAsyncService;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class AsyncServiceImpl implements IAsyncService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TestMapper testMapper;

    @Autowired
    private Executor executor;

    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync(List<Test> testList, TestMapper testMapper, CountDownLatch countDownLatch) {
        try {
            logger.warn("start executeAsync");
            testMapper.addTest(testList);
            logger.warn("end executeAsync");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("耗时异常：{}", e.getMessage());
        } finally {
            countDownLatch.countDown();//关键, 无论上面程序是否异常必须执行countDown,否则await无法释放
        }
    }

    @Override
    public int testMultiThread() {
        long startTime = System.currentTimeMillis();

        List<Test> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Test test = new Test();
            test.setId(String.valueOf(i));
            test.setName("name" + i);
            list.add(test);
        }

        /**
         * partition()方法 大家可以看一下
         * 例如 3001条数据 他会自动帮你分成两个数组 第一个数组3000条 第二个数组1条
         * 不需要我们再像以前一样 通过for循环处理截取
         */
        List<List<Test>> lists = Lists.partition(list, 5000);
        CountDownLatch countDownLatch = new CountDownLatch(lists.size());
        for (List<Test> tests : lists) {
            executeAsync(tests, testMapper, countDownLatch);
        }
        try {
            countDownLatch.await(); //保证之前的所有的线程都执行完成，才会走下面的；
            // 这样就可以在下面拿到所有线程执行完的集合结果
        } catch (Exception e) {
            logger.error("阻塞异常:" + e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        logger.info("耗时：" + time);
        return list.size();
    }


    @Override
    public int test2(List<Test> testList) {
        long startTime = System.currentTimeMillis();
        executor.execute(()->{
            testMapper.addTest(testList);
        });
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        logger.info("耗时：" + time);
        return testList.size();
    }
}
