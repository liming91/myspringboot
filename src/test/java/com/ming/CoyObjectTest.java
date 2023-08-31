package com.ming;

import com.ming.entities.Address;
import com.ming.entities.Teacher;
import org.junit.Test;

/**
 * 测试浅拷贝深拷贝
 *
 * @Author liming
 * @Date 2023/8/31 14:00
 */
public class CoyObjectTest {
    /**
     * 区别：
     * 深拷贝浅拷贝与对象克隆操作相关的概念：用于描述在复制对象时对对象内部引用的处理方式
     * <p>
     * 浅拷贝：在浅拷贝中，只有对象本身被复制，而不会复制对象内部的引用对象，
     * 也就是说复制的对象和原始对象共享内部引用对象，如果原始对象包含引用类型的属性，那么这些属性在浅拷贝后会指向相同的引用对象
     * <p>
     * 深拷贝：在深拷贝中不仅对象本身被复制，而且对象内部的引用对象也会被复制，从而创建一个全新的的独立对象。
     * 如果原始对象包含引用类型的属性，那么这些属性在深拷贝后会指向不通的引用对象。
     * <p>
     * 实际应用中：需要根据对象的结构和业务需求选择适当的拷贝方式，浅拷贝通常高效，但在需要独立对象的情况下可能会出现问题，深拷贝可以确保
     * 每个对象都是独立的，但在复杂对象结构中可能会引入更多的复杂性
     */
    @Test
    public void test1() {
        //地址
        Address address = new Address("北京");

        Teacher t1 = new Teacher("张三", address);

        //浅拷贝
        Teacher t2 = new Teacher(t1.getName(), t1.getAddress());

        //深拷贝
        //拷贝地址对象
        Address address3 = new Address(address.getCity());
        //拷贝老师对象
        Teacher t3 = new Teacher(t1.getName(), address3);

        //比较
        System.out.println("原始对象：" + t1.getAddress().getCity());
        System.out.println("浅拷贝对象：" + t2.getAddress().getCity());
        System.out.println("深拷贝对象：" + t3.getAddress().getCity());

        //修改对象的值
        t1.getAddress().setCity("上海");
        System.out.println("==============================修改后对象的值===============================");
        System.out.println("原始对象：" + t1.getAddress().getCity());
        System.out.println("浅拷贝对象：" + t2.getAddress().getCity());
        System.out.println("深拷贝对象：" + t3.getAddress().getCity());
    }
}
