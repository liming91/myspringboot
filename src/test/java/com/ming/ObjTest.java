package com.ming;

import cn.hutool.core.date.*;
import com.alibaba.fastjson.JSON;
import com.ming.bean.Dog;
import com.ming.bean.Order;
import com.ming.bean.User;
import com.ming.util.MathUtils;
import com.ming.util.PercentUtil;
import com.ming.util.WeekToDayUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

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

    @MockBean
    ServerEndpointExporter serverEndpointExporter;

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

        List<Order> list = new ArrayList<>();
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

        String flux = "import \"strings\" \n"
                + "from(bucket: \" status\")"
                + "  |> range(start: -5m, stop: now())   \n"
                + "  |> filter(fn: (r) => r[\"_measurement\"] == \"peidian\")   \n"
                + "  |> filter(fn: (r) => strings.containsStr(v: r[\"eqd\"], substr: \"Modbus.DB_\")) \n"
                + "  |> last()\n" + "  |> map(fn: (r) => ({ r with _value: string(v: r[\"_value\"]) }))\n"
                + "  |> group(columns: [\"eqd\"]) ";
        System.out.println(flux);
    }

    public static void main(String[] args) {

        String fileTime = "2024-02";
        //周
        String[] split = fileTime.split("\\-");
        int year = Integer.parseInt(split[0]);
        int week = Integer.parseInt(split[1]);

        //当前周的开始时间
        String startTime = WeekToDayUtil.weekToDayFisrtFormate(year, week);


        //获取上周的开始时间
        String lastStartTime = DateUtil.format(DateUtil.beginOfDay(DateUtil.offset(DateUtil.parse(startTime), DateField.WEEK_OF_YEAR, -1)), "yyyy-MM-dd");
        int weekCount = DateUtil.weekOfYear(DateUtil.parse(lastStartTime));


        System.out.println(startTime);
        System.out.println(lastStartTime);
        System.out.println(weekCount);

//        DateTime dateTime = DateUtil.lastWeek();
//        String format = DateUtil.format(dateTime, "yyyy-iw");
//        System.out.println(format);


    }
}
