package com.ming;

import cn.hutool.core.date.*;
import cn.hutool.core.lang.func.VoidFunc0;
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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
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
        order.setAmount(1.0);
        Order order2 = new Order();
        order2.setAmount(2.0);
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


    static Integer count = 0;
    private static ReentrantLock lock = new ReentrantLock();

    /**
     * 模拟超时
     * @return
     * @throws InterruptedException
     */
    private static String executeLongRunningOperation() throws InterruptedException {
        // 在这里执行你的数据库或SVN操作
        Thread.sleep(10000);
        return "Success";
    }


    @Test
    public void interfaceTimeOut(){
        final ExecutorService exec = Executors.newFixedThreadPool(1);
        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                executeLongRunningOperation();
                return "任务执行完成";
            }
        };


        try {
            Future<String> future = exec.submit(task);
            String obj = future.get(5, TimeUnit.SECONDS); //任务处理超时时间设为 1 秒
            System.out.println("任务成功返回:" + obj);
        }catch (TimeoutException e) {
            System.out.println("处理超时啦....");
        } catch (Exception e) {
            System.out.println("处理失败....");
            throw new RuntimeException(e);
        } finally {
            // 关闭线程池
            exec.shutdown();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        AtomicReference<Float> oMoney = new AtomicReference<>(1f);
        oMoney.updateAndGet(v1 -> v1 + Float.parseFloat("1.0"));
        System.out.println(oMoney.get());
    }
}
