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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * @Author liming
 * @Date 2023/9/1 15:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class RedisUtil {
    //缓存时间3分钟
    private static final long expTime = 3 * 60 * 1000;//ms
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1() {
        System.out.println("==========主要是测试jwt token 过期时间===========");
        long now = System.currentTimeMillis();
        Date nowDate = DateUtil.date();
        //jwt3分钟后过期 本来这个过期时间在jwt里面存在模拟存在redis的value中
        long exp = now + expTime;
        //redis缓存的过期时间
        long exps = DateUtil.between(DateUtil.date(now), DateUtil.date(exp), DateUnit.SECOND);


        //缓存的过期时间
        long redisUserExp = 0;


        if (redisTemplate.hasKey("token")) {
            redisUserExp = (long) redisTemplate.opsForValue().get("token");
        } else {
            redisTemplate.opsForValue().set("token", exp, exps, TimeUnit.SECONDS);
        }
        //将时间戳转date
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = f.format(Long.valueOf(redisUserExp));
        Date expRdiesDate = DateUtil.parse(d, "yyyy-MM-dd HH:mm:ss");

        //判断token过期 系统时间 -减去 exp过期时间（系统时间+缓存的时间）
        if (now - redisUserExp >= 0) {
            System.out.println("缓存的key：token过期");
        } else {
            System.out.println("缓存的key：token未过期");
        }

        //判断token过期 exp过期时间（系统时间+缓存的时间）-减去系统时间
        if (redisUserExp - now <= 0) {
            System.out.println("缓存的key：token过期");
        } else {
            System.out.println("缓存的key：token未过期");
        }
        //判断token过期 或者过期时间在系统时间之后过期
        boolean after = nowDate.after(expRdiesDate);

        if (after) {
            System.out.println("缓存的key：token过期");
        } else {
            System.out.println("缓存的key：token未过期");
        }
    }
}
