package com.ming.config;

import com.ming.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ Configuration作用：指明当前类是一个配置类，就是来替代之前的spring配置文件
 * 以前在配置文件中是用<benan></benan>添加组件的
 */
@Configuration
public class MyAppConfig {
    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 将方法的返回值添加到容器中,容器中这个组件默认的id就是方法名
     * @return
     */
    @Bean
    public HelloService helloService(){
        logger.info("配置类给容器中添加组件了...");
        return new HelloService();
    }
}
