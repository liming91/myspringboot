package com.ming;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @SpringBootApplication 来标注一个主程序类，说明是一个Springboot应用
 */
//@ImportResource(locations = "{classpath:bean.xml}")
@SpringBootApplication
//使用MapperScan扫描所有的mapper接口
@MapperScan(value = "com.ming.mapper")
public class MySpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySpringbootApplication.class, args);
    }

}
