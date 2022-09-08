package com.ming.entities;

/**
 * @Author liming
 * @Date 2022/8/23 9:32
 */
public class T1 extends Thread{

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println("子线程在执行");
        }
    }

    public static void main(String[] args) {
        T1 t1 = new T1();
        t1.start();
        for (int i = 0; i <3 ; i++) {
            System.out.println("主线程在执行");

        }
    }
}