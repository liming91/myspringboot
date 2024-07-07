package com.ming.entities;

/**
 * @Description
 * @Author liming
 * @Date 2024/7/2 23:06
 */
public class Dog extends Animal {

    @Override
    public void eat() {
        System.out.println("狗吃骨头");
    }

    @Override
    public void shout() {
        System.out.println("狗汪汪");
    }
}
