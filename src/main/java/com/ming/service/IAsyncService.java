package com.ming.service;

import com.ming.bean.Test;
import com.ming.mapper.TestMapper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public interface IAsyncService {

    public void executeAsync(List<Test> testList,TestMapper testMapper,CountDownLatch countDownLatch);
    int testMultiThread();

    int test2(List<Test> testList);

    int test3(List<Test> testList);

    List<Test> getTest();
}
