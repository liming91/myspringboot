package com.ming;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.system.oshi.OshiUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;


/**
 * @Author liming
 * @Date 2023/9/1 15:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class RedisUtil {
    //缓存时间ms
    private static final long expTime = 2 * 60 * 1000;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1() {
        System.out.println("主要是测试jwt token 过期时间");
        //3分钟后过期时间
        long now = System.currentTimeMillis();
        //过期时间
        long exp = now + expTime;
        long exps = DateUtil.between(DateUtil.date(now), DateUtil.date(exp), DateUnit.SECOND);

        //缓存的过期时间
        long redisUserExp = 0;
        if (redisTemplate.hasKey("exp")) {
            redisUserExp = (long) redisTemplate.opsForValue().get("exp");
        } else {
            redisTemplate.opsForValue().set("exp", exp, exps, TimeUnit.SECONDS);
        }
        //exp过期时间（系统时间+缓存的时间）-减去系统时间
        if (redisUserExp - now <= 0) {
            System.out.println("缓存的key：exp过期");
        } else {
            System.out.println("缓存的key：exp");
        }
    }
}
