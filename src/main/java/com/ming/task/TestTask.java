package com.ming.task;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.ming.entities.VO.InFoVO;
import com.ming.mapper.InfoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class TestTask {
    private final InfoMapper infoMapper;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void test1() {
        System.out.println("scheduler1 执行: " + Thread.currentThread() + "-" + DateTime.now());
        List<InFoVO> info = infoMapper.getInfo();
        log.info("数据：{}", JSON.toJSONString(info));
        info.forEach(x->{
            //数据库计划时间5分钟前抽发
            System.out.println(DateUtil.format(x.getTime(), DatePattern.NORM_DATETIME_PATTERN));
            DateTime dateTime = DateUtil.offsetMinute(x.getTime(), -5);
            String time = DateUtil.format(dateTime, DatePattern.NORM_DATETIME_MINUTE_PATTERN);
            String nowTime = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
            if(time.equals(nowTime)){
                log.info("计划时间：{},计划5分钟前时间：{}",x.getTime(),time);
            }
        });
    }
}
