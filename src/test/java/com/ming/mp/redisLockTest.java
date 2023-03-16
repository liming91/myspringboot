package com.ming.mp;

import com.ming.util.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author liming
 * @Date 2023/3/15 10:41
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = redisLockTest.class)
public class redisLockTest {
    @Autowired
    private RedisLockUtil redisLockUtil;
    private final static String KEY = "topic:"+System.currentTimeMillis();

    @Test
    public void Test() {
        if (!redisLockUtil.tryLock(KEY, 100)) {
            log.info("测试：加锁成功！");
            redisLockUtil.unLock(KEY);
            log.info("测试：释放锁成功");
        }
    }
}
