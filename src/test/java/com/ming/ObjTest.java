package com.ming;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Month;
import com.alibaba.fastjson.JSON;
import com.ming.bean.Dog;
import com.ming.bean.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author liming
 * @Date 2022/11/13 14:13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ObjTest {

    @Test
    public void obj1() throws IOException {
        String today = DateUtil.today();
        System.out.println("今天：" + today);


        int year = DateUtil.year(DateUtil.date());
        System.out.println("年：" + year);

        String month = DateUtil.format(new Date(), "yyyy");
        System.out.println(month);


        int dayOfWeek = DateUtil.dayOfWeek(new Date());
        System.out.println(dayOfWeek);
    }

    @Test
    public void obj2() {
        double d = 0.56789;
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
        System.out.println(bg.doubleValue());
    }


    @Test
    public void obj3() throws ParseException {

        List<Order> list  = new ArrayList<>();
        Order order = new Order();
        order.setAmount(0.0);
        Order order2 = new Order();
        order2.setAmount(0.0);
        list.add(order);
        list.add(order2);
        Double reduce = list.stream().map(Order::getAmount).reduce(0.0, (a, b) -> a + b);
        System.out.println(reduce);

    }


    @Test
    public void obj4() throws ParseException {

        List<Order> list  = new ArrayList<>();
        Order order = new Order();
        order.setAmount(0.0);
        Order order2 = new Order();
        order2.setAmount(0.0);
        list.add(order);
        list.add(order2);
        Double reduce = list.stream().map(Order::getAmount).reduce(0.0, (a, b) -> a + b);
        System.out.println(reduce);

    }
}
