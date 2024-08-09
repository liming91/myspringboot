package com.ming.config;

import com.ming.util.Threads;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
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

    int processNum = Runtime.getRuntime().availableProcessors(); // 返回可用处理器的Java虚拟机的数量

    int corePoolSize = (int) (processNum / (1 - 0.2));

    int maxPoolSize = (int) (processNum / (1 - 0.5));

    /**
     * ThreadPoolTaskScheduler:
     *
     * 用途: 主要用于调度任务。适合定时执行和周期性任务。
     * 功能: 提供了调度功能，例如一次性任务、固定延迟任务、定时任务等。它可以使用类似 Quartz 的调度功能来安排任务的执行。
     * 适用场景: 用于定期执行任务，例如每隔一定时间执行的任务，或基于 cron 表达式调度的任务。
     * ThreadPoolTaskExecutor:
     *
     * 用途: 主要用于执行异步任务。适合需要在后台执行的任务。
     * 功能: 负责将任务分配到线程池中执行。提供了常规的线程池管理功能，如线程池大小、任务队列等。
     * 适用场景: 用于处理并发任务、异步任务，例如处理用户请求、执行长时间运行的操作等。
     * 总结：ThreadPoolTaskScheduler 侧重于任务调度，而 ThreadPoolTaskExecutor 侧重于任务执行和并发处理。根据你的需求，选择合适的组件来管理和调度任务
     */
    @Bean("threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        int i = Runtime.getRuntime().availableProcessors();
        System.out.println("系统最大线程数：" + i);
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(i);
        taskExecutor.setMaxPoolSize(i);
        taskExecutor.setQueueCapacity(99999);
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setThreadNamePrefix("taskExecutor--");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(60);
        return taskExecutor;
    }

    @Bean(name = "threadPoolTaskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler syncScheduler = new ThreadPoolTaskScheduler();
        syncScheduler.setPoolSize(5);
        // 这里给线程设置名字，主要是为了在项目能够更快速的定位错误。
        syncScheduler.setThreadGroupName("syncTg");
        syncScheduler.setThreadNamePrefix("syncThread-");
        System.out.println("processNum===:"+processNum);
        syncScheduler.initialize();
        return syncScheduler;
    }


    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }

}
