package com.ming;

import com.ming.bean.Person;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MySpringBootApplicationTests {

    //记录器
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    Person person;

    @Autowired
    ApplicationContext ioc;

    @Autowired
    DataSource dataSource;

    @Test
    public void contextLoads(){
        System.out.println(person);
    }

    @Test
    public void helloService(){
        System.out.println(ioc.containsBean("helloService"));
    }

    @Test
    public void logTest(){
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
        System.out.println("===========================dataSource:"+dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("connection==:"+connection);
        connection.close();
    }






}
