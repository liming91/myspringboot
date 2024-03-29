package com.ming;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * @Author liming
 * @Date 2023/9/1 15:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class TokenDemoUtil {
    //缓存时间3分钟
    private static final long expTime = 3 * 60 * 1000;//ms

    public static final long EXPIRE = 24 * 2;



    @Autowired
    private RedisTemplate redisTemplate;


    public static void main(String[] args) {
        Date date = new Date(LocalDateTime.now().plusHours(EXPIRE).toInstant(ZoneOffset.of("+8")).toEpochMilli());
        System.out.println(DateUtil.format(date, DatePattern.NORM_DATETIME_PATTERN));
    }
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
        //jwt中校验token过期 JWTVerifier->verifyClaims->assertValidDateClaim
//        private void assertDateIsFuture(Date date, long leeway, Date today) {
//            today.setTime(today.getTime() - leeway * 1000);
//            if (date != null && today.after(date)) {
//                throw new TokenExpiredException(String.format("The Token has expired on %s.", date));
//            }
//        }

        if (after) {
            System.out.println("缓存的key：token过期");
        } else {
            System.out.println("缓存的key：token未过期");
        }
    }
}
