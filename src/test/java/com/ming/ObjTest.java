package com.ming;

import cn.hutool.core.date.*;
import cn.hutool.core.lang.func.VoidFunc0;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import com.alibaba.fastjson.JSON;
import com.ming.bean.Dog;
import com.ming.bean.Order;
import com.ming.bean.User;
import com.ming.util.MathUtils;
import com.ming.util.PercentUtil;
import com.ming.util.WeekToDayUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.jaxb.SourceType;
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
     *
     * @return
     * @throws InterruptedException
     */
    private static String executeLongRunningOperation() throws InterruptedException {
        // 在这里执行你的数据库或SVN操作
        Thread.sleep(10000);
        return "Success";
    }


    @Test
    public void interfaceTimeOut() {
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
        } catch (TimeoutException e) {
            System.out.println("处理超时啦....");
        } catch (Exception e) {
            System.out.println("处理失败....");
            throw new RuntimeException(e);
        } finally {
            // 关闭线程池
            exec.shutdown();
        }

    }


    public static String roomRatio(double v1, double v2, int scale) {
        if (scale < 0) {
            return "0";
        }
        BigDecimal b1 = new BigDecimal(v1 * 100);
        BigDecimal b2 = new BigDecimal(v2);
        if (b1.compareTo(BigDecimal.ZERO) == 0) {
            return Double.toString(BigDecimal.ZERO.doubleValue());
        }
        return Double.toString((b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue()));
    }


    public static void main(String[] args) {

        // 定义一个包含 6 个元素的 byte 数组
        byte[] data = new byte[6];

        // 添加两组数据
        data[0] = 1; // 第一组数据
        data[1] = 2;
        data[2] = 3;

        data[3] = 4; // 第二组数据
        data[4] = 5;
        data[5] = 6;

        // 输出数组内容
        for (byte b : data) {
            System.out.print(b + " ");
        }

        //接收到十六进制数据: 53 59 80 02 00 01 02 31 54 43
         byte[] data1 = new byte[]{83, 89, -128, 2, 0, 1, 2, 49, 84, 67};
        //byte[] data = new byte[]{83, 89, -128, 1, 0, 1, 0, 46, 84, 67};
        //byte[] data = new byte[]{83, 89, (byte) 131, 1, 0, 1, 0, 46, 84, 67};

        // 转换为十六进制字符串
        StringBuilder hexString = new StringBuilder();
        for (byte b : data) {
            hexString.append(String.format("%02X ", b));
        }
        System.out.println("接收到十六进制数据: " + hexString.toString().toLowerCase());
        if (data[0] == 0x53 && data[1] == 0x59) {
            switch (data[2]) {
                //人体存在
                case (byte) 0x80:
                    controlWord(data);
                    break;
                case (byte) 0x83:
                    fall(data);
                    break;
            }

        }


    }

    private static void fall(byte[] data) {
        System.out.println("跌倒");
        if (data[3] == 0x01) {//跌倒状态[83, 89, -128, 1, 0, 1, 0, 46, 84, 67]  16进制：53 59 80 01 00 01 00 2e 54 43
            StringBuilder havePersonStr = new StringBuilder();
            int havePersonLen = data[4] + data[5];
            for (int i = 0; i < havePersonLen; i++) {
                havePersonStr.append(data[6 + i]);
            }
            //将数据01的10进制转换为16进制
            String havePersonRes = String.format("%02X", Integer.parseInt(havePersonStr.toString()));
            switch (havePersonRes) {
                case "00":
                    System.out.println("未跌倒");
                    break;
                case "01":
                    System.out.println("跌倒");
                    break;
            }
        }
    }

    private static void controlWord(byte[] data) {
        switch (data[3]) {
            case 0x01://人员存在信息主动上报[83, 89, -128, 1, 0, 1, 0, 46, 84, 67]  16进制：53 59 80 01 00 01 00 2e 54 43
                StringBuilder havePersonStr = new StringBuilder();
                int havePersonLen = data[4] + data[5];
                for (int i = 0; i < havePersonLen; i++) {
                    havePersonStr.append(data[6 + i]);
                }
                //将数据01的10进制转换为16进制
                String havePersonRes = String.format("%02X", Integer.parseInt(havePersonStr.toString()));
                switch (havePersonRes) {
                    case "00":
                        System.out.println("无人");
                        break;
                    case "01":
                        System.out.println("有人");
                        break;
                }
                break;

            case 0x02://运动信息主动上报 [83, 89, -128, 2, 0, 1, 2, 49, 84, 67] 16进制：53 59 80 02 00 01 02 31 54 43
                StringBuilder sportStr = new StringBuilder();
                //接收到十六进制数据: 53 59 80 02 00 01 02 31 54 43
                int sportLen = data[4] + data[5];
                for (int i = 0; i < sportLen; i++) {
                    sportStr.append(data[6 + i]);
                }
                //将数据02的10进制转换为16进制
                String result = String.format("%02X", Integer.parseInt(sportStr.toString()));
                System.out.println(result);
                switch (result) {
                    case "00":
                        System.out.println("无");
                        break;
                    case "01":
                        System.out.println("静止");
                        break;
                    case "02":
                        System.out.println("活跃");
                        break;
                }
                break;
        }
    }


    public static int calculateChecksum(byte frameHeader, byte controlWord, byte commandWord, byte lengthIdentifier, byte[] data) {
        int sum = frameHeader & 0xFF;  // 转换为无符号整型
        sum += controlWord & 0xFF;
        sum += commandWord & 0xFF;
        sum += lengthIdentifier & 0xFF;

        // 添加数据部分
        for (byte b : data) {
            sum += b & 0xFF;
        }

        // 取低八位
        return sum & 0xFF;
    }
}
