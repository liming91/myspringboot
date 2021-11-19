package com.ming.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ExecutorConfig {

    Logger logger = LoggerFactory.getLogger(getClass());
//    @Value("async.executor.thread.core_pool_size")
//    private int corePoolSize;
//    @Value("${async.executor.thread.max_pool_size}")
//    private int maxPoolSize;
//    @Value("${async.executor.thread.queue_capacity}")
//    private int queueCapacity;
//    @Value("${async.executor.thread.name.prefix}")
//    private String namePrefix;

    @Bean(name = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        logger.warn("start asyncServiceExecutor");
        //在这里修改
        //ThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int processNum = Runtime.getRuntime().availableProcessors(); // 返回可用处理器的Java虚拟机的数量
        int corePoolSize = (int) (processNum / (1 - 0.2));
        logger.info("核心线程数:"+corePoolSize);
        int maxPoolSize = (int) (processNum / (1 - 0.5));
        logger.info("最大线程数:"+maxPoolSize);
        //配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        //配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(maxPoolSize * 1000);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

}
