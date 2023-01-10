package com.ming.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @Author liming
 * @Date 2022/8/23 9:55
 */
public class T3 implements Callable {
    @Override
    public String call() throws Exception {
        int sum=0;
        for (int i = 0; i < 3; i++) {
            sum+=i;
        }
        return "子线程计算结果："+sum;
    }

    public static void main(String[] args) {
        T3 t3 = new T3();
        FutureTask task = new FutureTask(t3);
        Thread t  = new Thread(task);
        t.start();;

    }
}