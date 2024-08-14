package com.ming;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author liming
 * @Date 2024/8/14 14:12
 */
public class ThreadTest {

    /**
     * 同步多线程处理业务并让主线程一直等待所有子线程执行完成后再返回
     *
     * 此方法中CountDownLatch会创建一个初始大小的计数器，每执行完一个子线程数量就会叠加，
     * 直到最后计数器满足数量时主线程才会执行到downLatch.await()下一步，但此方法虽简单，也有不足之处，无法接收所有子线程的结果
     */
    @Test
    public void synManyThread() {
        List<String> list = Arrays.asList("1", "2", "3");
        // 计数器
        CountDownLatch downLatch = new CountDownLatch(list.size());
        // 创建线程
        ExecutorService executorService = null;
        try {
            executorService = Executors.newCachedThreadPool();
            for (String item : list) {
                // 开启线程
                executorService.execute(() -> {
                    try {
                        // 业务处理
                        System.out.println(item);
                    } finally {
                        downLatch.countDown();
                    }
                });
            }
            downLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }

}
