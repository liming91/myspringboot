package com.ming;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author liming
 * @Date 2024/8/14 14:12
 */
public class ThreadTest {

    /**
     * 同步多线程处理业务并让主线程一直等待所有子线程执行完成后再返回
     *
     *
     *
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


    @Test
    public void futureTaskTest()  {
        try {


 //           List<Integer> list = new ArrayList<>();
//            CompletableFuture<List<Integer>> mapCompletableFuture = CompletableFuture.supplyAsync(() -> {
//                List<Integer> list1 = list1();
//                list.addAll(list1);
//                return list;
//            });
//            List<Integer> list1 = mapCompletableFuture.get(2, TimeUnit.SECONDS);
//            System.out.println(JSON.toJSONString(list1));
//
//
//            CompletableFuture<List<Integer>> mapCompletableFuture2 = CompletableFuture.supplyAsync(() -> {
//                List<Integer> list2 = list2();
//                list.addAll(list2);
//                return list;
//            });
//            List<Integer> list2 = mapCompletableFuture2.get(2, TimeUnit.SECONDS);
//            System.out.println(JSON.toJSONString(list1));


            List<Integer> list = new FutureTask<>(()->list1()).get(2,TimeUnit.SECONDS);
            System.out.println(JSON.toJSONString(list));

            List<Integer> list1 = new FutureTask<>(this::list2).get(2,TimeUnit.SECONDS);
            System.out.println(JSON.toJSONString(list1));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }


    public List<Integer>  list1() {
        try {
            List<Integer> list = new ArrayList<>();
            list.add(1);
            list.add(2);
            TimeUnit.SECONDS.sleep(30);
            return  list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer>  list2(){
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(4);
        return  list;
    }
}
