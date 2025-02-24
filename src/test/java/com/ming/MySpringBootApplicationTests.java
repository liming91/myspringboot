package com.ming;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Producer;
import com.ming.entities.Info;
import com.ming.service.IAsyncService;
import com.ming.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MySpringBootApplicationTests {


    //记录器
    Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    ApplicationContext ioc;

    @Autowired
    DataSource dataSource;
    @Autowired
    private IAsyncService iAsyncService;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;


    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1() {
        String capText = captchaProducerMath.createText();
        logger.info("验证码===：{}", capText);
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if (i == 2) {
                count += i;
                break;
            }
        }
        System.out.println("==================:" + count);
    }

    @Test
    public void helloService() {
        System.out.println(ioc.containsBean("helloService"));
    }


    /**
     * 日志的级别
     */
    @Test
    public void logTest() {
        //日志的级别由低到高trace<debug<info<warn<error
        //可以调整输出的日志的级别，日志只会在这个级别以后的高级别生效
        logger.trace("这是trace日志，跟踪框架轨迹...");
        logger.debug("这是debug调试信息日志...");
        //springboot默认使用info级别的,root级别,可以调整logging.level.com.ming=trace
        logger.info("这是info自己定义的输出日志..");
        logger.warn("这是警告日志..");
        logger.error("这是error日志...");
    }

    @Test
    public void jdbcTest() throws SQLException {
        //class org.apache.tomcat.jdbc.pool.DataSource
        System.out.println("===========================dataSource:" + dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("connection==:" + connection);
        connection.close();
    }


    @Test
    public void test3() {
//        List<String> list = new ArrayList<>();
//        list.add("11");
//        list.add("22");
//        redisTemplate.opsForList().leftPushAll("eq",list,1, TimeUnit.MINUTES);
//        List list1 = redisTemplate.opsForList().range("eq", 0, -1);
//        System.out.println(list1);

        List<Info> list = new ArrayList<>();
        Info info = new Info();
        info.setId("1");
        info.setName("ces");
        list.add(info);

//        // 将每个 User 对象序列化为 JSON 字符串
//        List<String> userJsonList = list.stream()
//                .map(JsonUtils::toJson).collect(Collectors.toList());
//        redisTemplate.opsForList().leftPushAll("eq",userJsonList);
//        redisTemplate.expire("eq", 10, TimeUnit.SECONDS);
//        List list1 = redisTemplate.opsForList().range("eq", 0, -1);
//        System.out.println(list1);
        HashSet<Info> data = new HashSet<>();
        Info info1 = new Info();
        info1.setId("1");
        info1.setName("ces");
        data.add(info1);

        Info info2 = new Info();
        info2.setId("1");
        info2.setName("ces");
        data.add(info2);
// 将每个 User 对象序列化为 JSON 字符串
        redisTemplate.opsForSet().add("equ_set", data.stream()
                .map(JsonUtils::toJson).distinct().toArray(String[]::new));

        Set<Info> equSet = getUsersFromRedis("equ_set");
        System.out.println(JSON.toJSONString(equSet));

        Set<String> keys = redisTemplate.keys("equ_set" + "*");
        System.out.println(keys);
    }


    public Set<Info> getUsersFromRedis(String key) {
        // 从 Redis 集合中获取所有 JSON 字符串
        Set<String> userJsonSet = redisTemplate.opsForSet().members(key);

        // 将 JSON 字符串集合反序列化为 User 对象集合
        Set<Info> users = userJsonSet.stream()
                .map(json -> JsonUtils.fromJson(json, Info.class))
                .collect(Collectors.toSet());

        return users;
    }
}
