package com.ming;


import cn.hutool.core.lang.UUID;
import com.ming.util.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @Author liming
 * @Date 2023/3/15 10:41
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisLockTest {

    @MockBean
    ServerEndpointExporter serverEndpointExporter;

    @Autowired
    private RedisLockUtil redisLockUtil;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final static String KEY = "key1:";
    String redisLockKey = String.format("%s:docker-images:%s", "LM_", "LOCK_IMAGE");
    String redisLockValue = UUID.randomUUID().toString();


    @Test
    public void Test() {
        try {
            if (redisLockUtil.tryLock(redisLockKey, 100)) {
                log.info("获取redis锁 [" + redisLockKey + "] 成功,执行以下业务逻辑!");
            } else {
                log.info("获取redis锁 [" + redisLockKey + "] 失败,锁占用!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            boolean releaseLock = redisLockUtil.unLock(redisLockKey);
            if (releaseLock) {
                log.info("释放redis锁 [" + redisLockKey + "] 成功!");
            } else {
                log.info("释放redis锁 [" + redisLockKey + "] 成功!");
            }
        }
    }


    @Test
    public void testRedisLock() throws InterruptedException {
        ArrayList<Thread> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            //logger.info("线程开始");
            Thread t = new Thread() {
                @Override
                public void run() {
                    if (redisLockUtil.tryLock(redisLockKey, 100)) {
                        try {
                            log.info("获取锁成功，继续执行任务" + Thread.currentThread().getName());
                            try {
                                TimeUnit.SECONDS.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            log.error("exception ", e);
                        } finally {
                            boolean releaseLock = redisLockUtil.unLock(redisLockKey);
                            if (releaseLock) {
                                log.info("释放redis锁 [" + redisLockKey + "] 成功!");
                            } else {
                                log.info("释放redis锁 [" + redisLockKey + "] 成功!");
                            }
                        }
                    }
                }
            };
            list.add(t);
            t.start();
        }
        for (Thread t : list) {
            t.join();
        }
        Thread.sleep(10000);
    }


    @Test
    public void Test2() {
        String lockKey = "lockKey";
        String clientId = UUID.randomUUID().toString();
        try {
            //如果键不存在则新增,存在则不改变已经有的值。存在返回 false，不存在返回 true
            Boolean flag = redisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 30, TimeUnit.SECONDS);
            if (!flag) {
                log.info("加锁失败");
            }
            if (redisTemplate.hasKey("stock")) {
                String stock = redisTemplate.opsForValue().get("stock").toString();
                int stockNum = Integer.parseInt(String.valueOf(stock));
                if (stockNum > 0) {
                    int realStockNum = stockNum - 1;
                    redisTemplate.opsForValue().set("stock", realStockNum);
                    log.info("扣减成功，剩余库存：{}", realStockNum);
                } else {
                    log.info("扣减失败，库存不足");
                }
            } else {
                System.out.println("重置stock");
                redisTemplate.opsForValue().set("stock", 30);
            }
        } finally {
            if (clientId.equals(redisTemplate.opsForValue().get(lockKey))) {
                redisTemplate.delete(lockKey);
                log.info("删除：{}", lockKey);
            }
        }
    }


}
