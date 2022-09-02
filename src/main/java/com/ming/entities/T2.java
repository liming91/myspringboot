package com.ming.entities;

/**
 * @Author liming
 * @Date 2022/8/23 9:52
 */
public class T2 implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i <3 ; i++) {
            System.out.println("子线程进行中");

        }
    }

    public static void main(String[] args) {
        T2 t2 = new T2();
        Thread t = new Thread(t2);
        t.start();
        for (int i = 0; i < 3; i++) {
            System.out.println("主线程正在执行~~");
        }
    }
}