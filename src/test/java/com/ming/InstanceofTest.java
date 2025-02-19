package com.ming;

import com.alibaba.fastjson2.JSONArray;
import com.ming.bean.User;
import com.ming.entities.Animal;
import com.ming.entities.Cat;
import com.ming.entities.Teacher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * instanceof 用法
 * @Author liming
 * @Date 2023/9/8 15:18
 */
public class InstanceofTest implements Cloneable{
    @Test
    public void test1() {
        //obj instance class obj是引用对象 class是类或者接口
        //obj是class的实例对象
        //obj是class的直接子类或者间接子类
        //obj是class接口的实现类

        Animal animal = new Cat();
        Cat cat = new Cat();
        Teacher teacher = new Teacher();

        InstanceofTest instanceofTest = new InstanceofTest();
        InstanceofTest clone = (InstanceofTest)instanceofTest.clone();

        //obj instance class obj是引用对象 class是类或者接口
        if(animal instanceof Animal){
            System.out.println("obj是class的实例对象");
        }
        //Dog dog = (Dog) animal;
        if(animal instanceof Cat){
            Cat cat1 = (Cat) animal;
            System.out.println("obj是class的直接子类或者间接子类");
        }
        ArrayList arrayList = new ArrayList();
        if (arrayList instanceof List) {
            System.out.println("obj是class接口的实现类");
        }
    }

    @Override
    public Object clone() {
        try {
            return  super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
