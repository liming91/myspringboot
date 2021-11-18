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

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class AsyncServiceImpl implements IAsyncService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TestMapper testMapper;

    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync(List<Test> testList) {
        CountDownLatch countDownLatch =new CountDownLatch(testList.size());
        try {
            long startTime = System.currentTimeMillis();
            logger.warn("start executeAsync");
            List<Test> select = testMapper.select();
            logger.info("size===:{}"+select.size());
            testMapper.addTest(testList);
            logger.warn("end executeAsync");
            long endTime  = System.currentTimeMillis();
            long time = endTime-startTime;
            logger.info("耗时："+time);
            try {
                countDownLatch.await(); //保证之前的所有的线程都执行完成，才会走下面的；
                // 这样就可以在下面拿到所有线程执行完的集合结果
            } catch (Exception e) {
                logger.error("阻塞异常:"+e.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("耗时异常：{}", e.getMessage());
        }finally {
            countDownLatch.countDown();//关键, 无论上面程序是否异常必须执行countDown,否则await无法释放
        }
    }
}
