package com.ming.thread;

/**
 * @Author liming
 * @Date 2023/1/9 15:44
 */
public class TestThread {


    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());

        T1 t1 = new T1();
        t1.start();


        T2 t2 = new T2();
        t2.start();
    }
}
