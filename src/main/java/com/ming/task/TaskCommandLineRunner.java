package com.ming.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.ming.entities.Info;
import com.ming.entities.InfoTask;
import com.ming.mapper.InfoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务初始化
 * @Author liming
 * @Date 2022/10/8 16:43
 */
@Slf4j
@Component
@AllArgsConstructor
public class TaskCommandLineRunner implements CommandLineRunner {
    private final DynamicTask dynamicTask;

    private final InfoMapper infoMapper;
    /**
     * 每分钟查询加入到队列
     */
//    @Scheduled(cron = "0 0/1 * * * ? ")
//    public void runTask() {
//        initTask();
//    }
    @Override
    public void run(String... strings) throws Exception {
        initTask();
    }

    public void initTask(){
        List<Info> info = infoMapper.selectList(null);
        if(CollectionUtil.isNotEmpty(info)){
            info.forEach(x ->{
                String infoTime = DateUtil.format(x.getTime(), DatePattern.NORM_DATETIME_PATTERN);
                LocalDateTime time = LocalDateTimeUtil.parse(infoTime, DatePattern.NORM_DATETIME_PATTERN);
                InfoTask build = InfoTask.builder()
                        .id(x.getId())
                        .start(time)
                        .taskInfo(x).build();
                dynamicTask.add(build);
            });
            log.info("测试任务消息推送初始化成功，共计 {} 条",info.size());
        }
    }
}
