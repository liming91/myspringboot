package com.ming;

import com.ming.entities.Animal;
import com.ming.entities.Dog;

/**
 * 多态测试
 * @Description
 * @Author liming
 * @Date 2024/7/2 23:19
 */
public class AnimalTest {

    public static void main(String[] args) {
        AnimalTest animalTest = new AnimalTest();
        animalTest.fun(new Dog());
    }

    public void fun(Animal animal) {
        animal.eat();
        animal.shout();
    }
}
