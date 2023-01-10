package com.ming.thread;

/**
 * @Author liming
 * @Date 2022/8/23 9:32
 */
public class T1 extends Thread{
    public static Object obj1  = new Object();

    @Override
    public void run() {
        //死锁测试
        System.out.println("T1 run()" + Thread.currentThread().getName());
        synchronized (obj1){

            System.out.println("获取obj1");
            try {
                sleep(100);
                synchronized (T2.obj2){
                    System.out.println("获取obj2");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}