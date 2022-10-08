package com.ming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 *  异步线程池ThreadPoolExecutor 配置类
 * @Author liming
 * @Date 2022/10/8 17:14
 */
@Configuration
public class ThreadPoolTaskExecutorConfig {
    @Bean
    public ThreadPoolTaskScheduler syncScheduler() {
        ThreadPoolTaskScheduler syncScheduler = new ThreadPoolTaskScheduler();
        syncScheduler.setPoolSize(5);
        // 这里给线程设置名字，主要是为了在项目能够更快速的定位错误。
        syncScheduler.setThreadGroupName("syncTg");
        syncScheduler.setThreadNamePrefix("syncThread-");
        syncScheduler.initialize();
        return syncScheduler;
    }
}
