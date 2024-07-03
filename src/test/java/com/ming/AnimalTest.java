package com.ming;

import com.ming.entities.Animal;
import com.ming.entities.Cat;
import com.ming.entities.Dog;

import java.sql.Connection;

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
        animalTest.fun(new Dog());
        animalTest.fun(new Cat());

        animalTest.method(new Cat());
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
