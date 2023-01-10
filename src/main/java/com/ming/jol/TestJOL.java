package com.ming.jol;

import org.openjdk.jol.info.ClassLayout;

/**
 * @Author liming
 * @Date 2023/1/9 16:12
 */
public class TestJOL {

    static class T1 {

    }


    public static void main(String[] args) {
        T1 t1 = new T1();
        System.out.println(System.identityHashCode(t1));
        System.out.println(ClassLayout.parseInstance(t1).toPrintable());
        System.out.println("======================");
        synchronized (t1) {
            System.out.println(ClassLayout.parseInstance(t1).toPrintable());
        }
        System.out.println("======================");
        System.out.println(ClassLayout.parseInstance(t1).toPrintable());

    }
}
