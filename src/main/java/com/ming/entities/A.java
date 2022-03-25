package com.ming.entities;

public class A extends Thread{

    @Override
    public void run() {
    synchronized (A.class){
        for (int i =1;i<10;i++){
            System.out.println(Thread.currentThread().getName()+":"+i);
        }
    }

    }
}
