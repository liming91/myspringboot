package com.ming.entities;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @Author liming
 * @Date 2022/8/23 9:55
 */
public class T3 implements Callable<String> {
    @Override
    public String call() throws Exception {
        int sum=0;
        for (int i = 0; i < 3; i++) {
            sum+=i;
        }
        return "子线程计算结果："+sum;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        T3 t3 = new T3();
        FutureTask<String> task = new FutureTask<>(t3);
        Thread t  = new Thread(task);
        t.start();
    }
}