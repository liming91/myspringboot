package com.ming.entities;


public class B implements Runnable{
    int i =1;
    @Override
    public void run() {
      while (true){
          synchronized (this){
              notify();
              try {
                  Thread.sleep(100);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              if(i<=10){
                  System.out.println(Thread.currentThread().getName()+":"+i);
                  i++;
                  try {
                      wait();
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }else{
                  break;
              }

          }

      }
    }

    public static void main(String[] args) {
//        B b = new B();
//        Thread t1 = new Thread(b);
//        Thread t2 = new Thread(b);
//        t1.setName("B1");
//        t2.setName("B2");
//        t1.start();
//        t2.start();

    }
}
