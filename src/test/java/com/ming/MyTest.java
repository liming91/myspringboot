package com.ming;

import com.ming.entities.FA;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
                new FA("4", "D", 4, 4000.88),
                new FA("5", "E", 5, 3000.88)
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






}
