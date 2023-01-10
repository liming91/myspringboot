package com.ming.service;

import cn.hutool.core.convert.ConverterRegistry;
import com.alibaba.fastjson.JSON;
import com.ming.entities.InfoTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

/**
 * 任务执行器
 * @Author liming
 * @Date 2022/10/8 17:11
 */
@Slf4j
@Component
public class DynamicTask {
    /**
     * 以下两个都是线程安全的集合类。
     */
    public Map<String, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();
    public List<InfoTask> taskList = new CopyOnWriteArrayList<InfoTask>();


    private final ThreadPoolTaskScheduler syncScheduler;

    public DynamicTask(ThreadPoolTaskScheduler syncScheduler) {
        this.syncScheduler = syncScheduler;
    }

    /**
     * 查看已开启但还未执行的动态任务
     * @return
     */
    public List<InfoTask> getTaskList() {
        return taskList;
    }

    /**
     * 运行任务
     * @param task
     * @return
     */
    public Runnable getRunnable(InfoTask task) {
        return () -> {
            log.info("测试任务消息推送开始");
            System.out.println("此时时间==>" + LocalDateTime.now());
            log.info("测试任务消息推送完成:{}", JSON.toJSONString(task));
        };
    }

    /**
     * 添加一个动态任务
     * @param task
     * @return
     */
    public boolean add(InfoTask task) {
        // 此处的逻辑是 ，如果当前已经任务ID存在，先删除之前的，再添加现在的。（即重复就覆盖）
        if (null != taskMap.get(task.getId())) {
            stop(task.getId());
        }
        // 转换类型
        ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
        Date startTime = converterRegistry.convert(Date.class, task.getStart());

        // schedule :调度给定的Runnable ，在指定的执行时间调用它。
        //一旦调度程序关闭或返回的ScheduledFuture被取消，执行将结束。
        //参数：
        //任务 – 触发器触发时执行的 Runnable
        //startTime – 任务所需的执行时间（如果这是过去，则任务将立即执行，即尽快执行）
        ScheduledFuture<?> schedule = syncScheduler.schedule(getRunnable(task), startTime);
        taskMap.put(task.getId(), schedule);
        taskList.add(task);
        return true;
    }

    /**
     * 停止/删除任务
     * @param id
     * @return
     */
    public boolean stop(String id) {
        if (null == taskMap.get(id)) {
            return false;
        }
        ScheduledFuture<?> scheduledFuture = taskMap.get(id);
        scheduledFuture.cancel(true);
        taskMap.remove(id);
        taskList.removeIf(x -> x.getId().equals(id));
        return true;
    }
}
