package com.ming;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ming.entities.*;
import lombok.Builder;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

public class MyTest {
    Logger log = LoggerFactory.getLogger(MyTest.class);

    @Test
    public void commonExtendsTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int sum = list.stream()
                .reduce(0, (x, y) -> x + y);
        System.out.println(sum);

        List<FA> employeeList = Arrays.asList(
                new FA("1", "A", 1, 8000.88),
                new FA("2", "B", 2, 7000.88),
                new FA("3", "C", 3, 9000.88),
                new FA("4", "D", 4, 0D),
                new FA("5", "E", 5, 0D)
        );

        Integer integer = employeeList.stream().map(FA::getAge).reduce(Integer::sum).get();
        System.out.println(integer);
        FA fa = new FA();
        Comparator<FA> consumer = (x, y) -> Integer.compare(x.getAge(), y.getAge());
        Comparator<FA> comparator = new Comparator<FA>() {

            @Override
            public int compare(FA o1, FA o2) {
                return 0;
            }
        };
        employeeList.sort(consumer);

        Arrays.sort(new List[]{employeeList});

        


    }

    @Test
    @Builder
    public void dishTest1() {
//        String a ="2022-08-16T10:21:52.065+08:00";
//        DateTimeFormatter dtf1 = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        DateTime dt= dtf1.parseDateTime(a);
//        DateTimeFormatter dtf2= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
//        String s = dt.toString(dtf2);
//        System.out.println(s);
//
//        String a ="2022-08-16T10:21:52.065+08:00";
//        String format1 = DateUtil.format(DateUtil.parse(a), DatePattern.NORM_DATETIME_MS_FORMAT);
//        DateTime parse = DateUtil.parse(format1, DatePattern.NORM_DATETIME_PATTERN);
//        String format = DateUtil.format(parse, DatePattern.NORM_DATETIME_PATTERN);
//        System.out.println(format);



    }


    public static void main(String[] args) {

    }




}
