package com.ming.service.impl;

import com.ming.service.IForDependService1;
import com.ming.service.IForDependService2;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author liming
 * @Date 2023/10/20 16:09
 */
@Slf4j
@Service
@AllArgsConstructor
public class ForDependService2Impl implements IForDependService2 {

    private final IForDependService1 iForDependService1;

    @Override
    public void getDepend2() {
        log.info("ForDependService2Impl:{}","调用services1方法");
        iForDependService1.getDependService();
    }
}
