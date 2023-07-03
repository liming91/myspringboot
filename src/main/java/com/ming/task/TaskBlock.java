package com.ming.task;

import cn.hutool.core.date.DateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 模拟测试任务阻塞
 * 因为是单线程池执行的
 * @Author liming
 * @Date 2023/7/3 10:01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskBlock {


    @Scheduled(cron = "0/1 * * * * ?")
    public void test1() {
        // 每1s执行一次
        System.out.println("scheduler1 执行: " + Thread.currentThread() + "-" + DateTime.now());
        try {
            Thread.sleep(5*1000);  // 5s
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * 可以看到，第一个定时任务没执行完时，第二个定时任务也是被阻塞的。而且是同一个线程去执行的这两个定时任务。
     */
    @Scheduled(cron = "0/2 * * * * ?")
    public void test2() {
        // 每2s执行一次
        System.out.println("scheduler2 执行: " + Thread.currentThread() + "-" + DateTime.now());
    }
}
