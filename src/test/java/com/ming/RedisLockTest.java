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
    private final static String KEY = "key1:"+System.currentTimeMillis();

    @Test
    public void Test() {
        if (redisLockUtil.tryLock(KEY, 100)) {
            log.info("测试：加锁成功！");
            redisLockUtil.unLock(KEY);
            log.info("测试：释放锁成功！");
        } else {
            System.out.println("测试：加锁失败！");
        }

    }


    @Test
    public void  Test2(){
        String lockKey = "lockKey";
        String clientId = UUID.randomUUID().toString();
        try {
            Boolean flag = redisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 30, TimeUnit.SECONDS);
            if (!flag) {
                log.info("加锁失败");
            }
            if(redisTemplate.hasKey("stock")){
                String stock = redisTemplate.opsForValue().get("stock").toString();
                int stockNum = Integer.parseInt(String.valueOf(stock));
                if (stockNum > 0) {
                    int realStockNum = stockNum - 1;
                    redisTemplate.opsForValue().set("stock", realStockNum);
                    log.info("扣减成功，剩余库存：{}", realStockNum);
                } else {
                    log.info("扣减失败，库存不足");
                }
            }else{
                System.out.println("重置stock");
                redisTemplate.opsForValue().set("stock", 30);
            }
        } finally {
            if (clientId.equals(redisTemplate.opsForValue().get(lockKey))) {
                redisTemplate.delete(lockKey);
                log.info("删除：{}",lockKey);
            }
        }
    }


}
