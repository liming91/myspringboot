package com.ming;

import com.ming.entities.Animal;
import com.ming.entities.Cat;
import com.ming.entities.Dog;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 多态测试
 * @Description
 * @Author liming
 * @Date 2024/7/2 23:19
 */
public class AnimalTest {

    public static void main(String[] args) {
        //多态
        AnimalTest animalTest = new AnimalTest();
        System.out.println(animalTest.getClass().getSuperclass());
        animalTest.fun(new Dog());
        animalTest.fun(new Cat());

        animalTest.method(new Cat());

        Animal animal = new Cat();
        //obj instance class obj是引用对象 class是类或者接口
        if(animal instanceof Animal){
            System.out.println("obj是class的实例对象");
        }
        //Dog dog = (Dog) animal;
        if(animal instanceof Cat){
            Cat cat = (Cat) animal;
            System.out.println("obj是class的直接子类或者间接子类");
        }
        ArrayList arrayList = new ArrayList();
        if (arrayList instanceof List) {
            System.out.println("obj是class接口的实现类");
        }
    }

    //多态
    public void fun(Animal animal) {
        animal.eat();
        animal.shout();
    }

    //如果没有多态性，必须创建子类对象
    public void fun(Dog dog) {
        dog.eat();
        dog.shout();
    }

    public void fun(Cat cat) {
        cat.eat();
        cat.shout();
    }

    //有多态、随便new个对象可以通用 比如调用animalTest.method(new Cat());
    public void method(Object obj){

    }

    //多态 jdbc中调用不同的数据库厂商,相当于在父类中定义了数据库连接，我们操作调用时只需要对应不同的数据库厂商
     class Driver{
        public void doData(Connection conn){//相当于 new MySqlConnection() 的子类
                //按照规范操作数据
                //conn.createStatement()
        }
    }

}
