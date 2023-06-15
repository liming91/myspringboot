package com.ming.singleton;

/**
 * 单例
 * 饿汉式(线程安全的):在加载类的时候就会创建类的单例，并保存在类中。
 * 1.定义类变量实例并直接实例化，在类加载的时候就完成了实例化并保存在类中;
 * 2.定义无参构造器，用于单例实例.
 * 3.定义公开方法，返回已创建的单例.
 * 懒汉式(线程不安全的): 懒汉式就是不在系统加载时就创建类的单例，而是在第一次使用实例的时候再创建。
 * 1.定义一个私有类变量来存储单例,私有的目的是指外部无法直接获取这个变量，而要使用提供的公共方法来获取.
 * 2.定义私有构造器，表示只在类内部使用，亦指单例的实例只能在单例类内部创建
 * 3.定义一个公共的公开的方法来返回该类的实例，由于是懒汉式，需要在第一次使用时生成实例，所以为了线程安全，
 * 使用synchronized关键字来确保只会生成单例.
 * 代理模式
 *
 * @Author liming
 * @Date 2023/6/15 14:59
 */
public class SingletonTest {
    //饿汉在类加载的时候创建类的单例
/*    //1、创建类的实例变量
    private SingletonTest singletonTest = new SingletonTest();

    //2、提供一个无餐构造器
    private SingletonTest() {
    }

    //3、提供一个静态方法
    public SingletonTest getInstance() {
        return singletonTest;
    }*/

    //懒汉在调用时创建单例对象
    //1、提供一个私有的实例变量
    private static SingletonTest singletonTest;

    //2、定义私有构造器
    private SingletonTest() {
    }



    //3、提供公共方法
    public static SingletonTest getInstance() {
        if (singletonTest == null) {
            synchronized (SingletonTest.class) {
                if (singletonTest == null) {
                    singletonTest = new SingletonTest();
                }
            }
        }
        return singletonTest;
    }


}



