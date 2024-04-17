package com.ming.mp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @Author liming
 * @Date 2024/4/17 14:19
 */
public class ErrorTest {

    @Test
    public void concurrentModificationExceptionTest(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(2);
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Integer integer = iterator.next();
            if (integer == 2)
                list.remove(integer);
        }
    }
}
