package com.ming.service.impl;

import com.ming.service.IForDependService1;
import com.ming.service.IForDependService2;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 循环依赖测试
 * @Author liming
 * @Date 2023/10/20 16:06
 */
@Slf4j
@Service
public class ForDependService1Impl implements IForDependService1 {

    @Autowired
    private  IForDependService2 iForDependService2;

    @Override
    public void getDependService() {
        log.info("ForDependService1Impl:{}","调用services2方法");
        iForDependService2.getDepend2();
    }
}
