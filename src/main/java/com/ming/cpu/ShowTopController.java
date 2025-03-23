package com.ming.cpu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 线上cpu飙高，死循环，死锁排查
 */
@RestController
@RequestMapping("top")
public class ShowTopController {

    Logger logger = LoggerFactory.getLogger(ShowTopController.class);

    private Object lock1 = new Object();
    private Object lock2 = new Object();

    /**
     * 死循环
     *
     * @return
     */
    @RequestMapping("loop")
    public String loop() {
        logger.info("死循环产生");
        System.out.println("死循环产生");
        while (true) {
        }
    }

    /**
     * 死锁
     *
     * @return
     */
    @RequestMapping("deadLock")
    public String deadLock() {
        new Thread(() -> {
            synchronized (lock1) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    synchronized (lock2) {
                        System.out.println("thread1 over");
                    }
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (lock2) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    synchronized (lock1) {
                        System.out.println("thread2 over");
                    }
                }
            }
        }).start();
        return "success";
    }

}
