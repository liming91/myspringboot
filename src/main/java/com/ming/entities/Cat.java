package com.ming.entities;

/**
 * @Description
 * @Author liming
 * @Date 2024/7/2 23:06
 */
public class Cat extends Animal {

    @Override
    public void eat() {
        System.out.println("猫吃鱼");
    }

    @Override
    public void shout() {
        System.out.println("猫喵喵喵");
    }
}
