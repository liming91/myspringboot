package com.ming;


import com.ming.util.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

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


}
