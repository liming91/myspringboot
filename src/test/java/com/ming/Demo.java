package com.ming;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.min;
import org.junit.Test;

import java.io.OutputStream;
import java.sql.SQLOutput;
import java.util.Arrays;

@Slf4j
public class Demo {

    @Test
    public void arrays() {

        int a; //变量的声明
        a = 1;//初始化
        int b = 1;//声明和初始化

        int array[]; //数组声明
        array = new int[2];  //初始化(数组是引用数据类型、必须是new关键字)
        //①静态初始化：数组的初始化和数组元素的赋值操作同时进行
        int n[] = new int[]{1, 2, 3};
        //②动态初始化：数组的初始化和数组的元素分开进行
        int n1[] = new int[3];

        String s[] = new String[]{"12"};
        for (int i = 0; i < s.length; i++) {
            System.out.println(s[i]);
        }
        int cd = s.length;
        System.out.println(cd);
        String s1 = s[0];
        char c = s1.charAt(1);
        System.out.println(c);

    }

    @Test
    public void maxArrays() {
        int[] array = {10, 22, 34};
        int max = 0;
//        for (int i = 0; i < array.length; i++) {
//            array[i] =(int) ((Math.random() * (99-10+1)) + 10);
//        }
        for (int i = 0; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }

        }

        System.out.println(max);
    }


    @Test
    public void minArrays() {
        int[] array = {10, 22, 34};
        int min = array[0];
//        for (int i = 0; i < array.length; i++) {
//            array[i] =(int) ((Math.random() * (99-10+1)) + 10);
//        }
        for (int i = 0; i < array.length; i++) {
            if (min > array[i]) {
                min = array[i];
            }

        }
        System.out.println(min);
    }


    @Test
    public void copyArrays() {
        String[] array1 = {"a", "b"};
        //复制操作就是在堆内存中在创建一个数组、数组长度为复制的数组的length
        String[] array2 = new String[array1.length];
        for (int i = 0; i < array1.length; i++) {
            array2[i] = array1[i];
        }
    }

    @Test
    public void arrays3() {
        int[] array = new int[6];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) ((Math.random() * 30) + 1);
            for (int j = 0; j < i; j++) {
                if (array[i] == array[j]) {
                    i--;
                    break;
                }
            }
        }
        //遍历
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }


}
