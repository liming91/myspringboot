package com.ming.thread;

/**
 * @Author liming
 * @Date 2022/8/23 9:52
 */
public class T2 extends Thread{
    public static Object obj2  = new Object();
    @Override
    public void run() {
        //死锁测试
        System.out.println("T2 run()" + Thread.currentThread().getName());
        synchronized (obj2){
            System.out.println("获取obj2");
            try {
                sleep(100);

                synchronized ( T1.obj1){
                    System.out.println("获取obj1");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}