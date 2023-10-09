package com.ming.task;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.ming.entities.VO.InFoVO;
import com.ming.mapper.InfoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class TestTask {
    private final InfoMapper infoMapper;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void test1() {
        System.out.println("scheduler1 执行: " + Thread.currentThread() + "-" + DateTime.now());
        List<InFoVO> info = infoMapper.getInfo();
        log.info("数据：{}", JSON.toJSONString(info));
    }
}
