package com.ming.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author liming
 * @Date 2023/10/20 16:09
 */
@Slf4j
@Service
public class ForDependService2Impl {


    private  ForDependService1Impl forDependService1;

    @Autowired
    public ForDependService2Impl(ForDependService1Impl forDependService1) {
        this.forDependService1 = forDependService1;
    }
}
