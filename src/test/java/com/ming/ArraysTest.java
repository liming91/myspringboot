package com.ming;

import com.ming.bean.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ArraysTest {

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
    public void arraysFen() {
        //二分法查找叫折半查找
        int a = -7;
        int array[] = {23, 33, 12, 3, -7, 55, 89, 0, 13, 11};
        boolean flag = true;
        int head = 0;//头部索引
        int end = array.length - 1;//尾部索引
        int mid = (head + end) / 2;//中间索引
        while (head < end) {
            if (a == array[mid]) {
                System.out.println("找到了指定元素，位置为：" + mid);
                flag = false;
                break;
            } else if (array[mid] > a) {
                end = mid - 1;
            } else {//array[mid]<a
                head = mid + 1;
            }
        }

        if (flag) {
            System.out.println("没有找到");
        }
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
    public void maopao() {
        int array[] = {44, 33, 12, 3, -7, 55, 89, 0, 13, 11};
        //array.length-1 外层长度减一 最后一个元素不需要比较 也可以比较不用减一
        for (int i = 0; i < array.length - 1; i++) {
            //array.length-1-i
            // 内层长度：就是数组长度相邻比较后的次数就是长度减一
            // 减i:就是每比一次少个元素进行减i
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;

                }

            }
        }

        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + "\t");
        }
    }


    @Test
    public void quickSort() {



        Student[] students = new Student[20];
        for (int i = 0; i < students.length; i++) {
            Student student = new Student();
            student.setNumber((int) Math.round(Math.random()*20));
            student.setScore((int) Math.round(Math.random()*20));
            student.setState((int) Math.round(Math.random()*20));
            if (student.getState() == 3) {
                System.out.println(student);
            }
        }
    }


}
