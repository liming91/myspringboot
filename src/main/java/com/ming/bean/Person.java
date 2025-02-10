package com.ming.bean;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 将配置文件yml中的每一个属性的值，映射到这个组件中
 *
 * @ ConfigurationProperties:告诉springboot将本类中的所有属性和配置文件中相关的配置进行绑定
 * prefix = "person":配置文件中哪个下面的所有属性进行一一映射
 * 只有是容器中的组件，才能使用容器提供的功能,加入@Component
 */

/**
 * 类的成员构成
 */



public class Person implements Serializable {


    //属性或成员变量
     String pName;
     int age=1;

    //构造器
    public Person() {
super();
    }

    public Person(String name) {
        this.pName = name;
    }

    //方法或者函数
    public String run() {
        System.out.println("跑步");
        return "run....";
    }

                //代码块
                {
                    pName = "周杰伦";
                }

    //内部类
    class Dog{
        String dName;
    }

    public static void main(String[] args) {
        String  s = "002_A";
        System.out.println(s.substring(0,s.indexOf("_")));
        System.out.println(s.substring(s.lastIndexOf("_")+1,s.length()));
    }
}




