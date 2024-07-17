package com.ming;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ming.bean.User;
import com.ming.entities.*;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

public class MyTest {
    Logger log = LoggerFactory.getLogger(MyTest.class);

    @Test
    public void commonExtendsTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

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

    /**
     * 按名字统计今天昨天出现的次数
     */
    @Test
    public void nameCount() {
        String today = DateUtil.today();
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", "刘敏");
        dataMap.put("time", "2022-10-13");
        Map<String, Object> dataMap1 = new HashMap<>();
        dataMap1.put("name", "谷满");
        dataMap1.put("time", "2022-10-13");
        Map<String, Object> dataMap2 = new HashMap<>();
        dataMap2.put("name", "吴淅波");
        dataMap2.put("time", "2022-10-13");
        Map<String, Object> dataMap3 = new HashMap<>();
        dataMap3.put("name", "谷满");
        dataMap3.put("time", "2022-10-14");
        Map<String, Object> dataMap4 = new HashMap<>();
        dataMap4.put("name", "鲁甲豪");
        dataMap4.put("time", "2022-10-14");
        dataList.add(dataMap);
        dataList.add(dataMap2);
        dataList.add(dataMap1);
        dataList.add(dataMap3);
        dataList.add(dataMap4);
        System.out.println(JSON.toJSONString(dataList));
        System.out.println("===================================");
        Map<String, List<Map<String, Object>>> timeMap = dataList.stream().collect(Collectors.groupingBy(d -> String.valueOf(d.get("time"))));
        Set<String> nameSet = dataList.stream().map(m -> String.valueOf(m.get("name"))).collect(Collectors.toSet());
        List<HashMap<String, Object>> collect = nameSet.stream().map(m -> {
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("name", m);
            objectObjectHashMap.put("todayCount", 0);
            objectObjectHashMap.put("lastCount", 0);
            return objectObjectHashMap;
        }).collect(Collectors.toList());
        for (Map.Entry<String, List<Map<String, Object>>> timeList : timeMap.entrySet()) {
            String key1 = timeList.getKey();
            List<Map<String, Object>> timeListValue = timeList.getValue();
            Map<String, List<Map<String, Object>>> map = timeListValue.stream().collect(Collectors.groupingBy(d -> String.valueOf(d.get("name"))));
            for (Map.Entry<String, List<Map<String, Object>>> nameList : map.entrySet()) {
                String name = nameList.getKey();
                int size = nameList.getValue().size();
                if (key1.equals(today)) {
                    collect.forEach(n -> {
                        if (name.equals(String.valueOf(n.get("name")))) {
                            n.put("todayCount", size);
                        }
                    });
                } else {
                    collect.forEach(n -> {
                        if (name.equals(String.valueOf(n.get("name")))) {
                            n.put("lastCount", size);
                        }
                    });
                }
            }

        }

        System.out.println("返回结果==：" + JSON.toJSONString(collect));
    }

    //求1-100的和
    public static int getSum(int n) {
        if (n == 1) {
            return 1;
        } else {
            int t = getSum(n - 1);
            System.out.println("n==:" + n);
            System.out.println("t==:" + t);
            return n + t;
        }
    }


    @Test
    public void beforeDate() {
        String createTime = "2023-03-13 23:38:53";
        String notificationMaxTime = "2023-03-15 23:38:53";
        if (DateUtil.parse(createTime, DatePattern.NORM_DATETIME_PATTERN).isAfter(DateUtil.parse(notificationMaxTime, DatePattern.NORM_DATETIME_PATTERN))) {
            //发送预警消息
            System.out.println("发送");
        } else {
            System.out.println("不发送");
        }
    }


    /**
     * 实时时间格式化到小时
     *
     * @param num
     * @return
     */
    public static String getLocalDateTimeHour(Integer num) {
        java.time.format.DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        // 当天日期
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(num);
        // 格式化时间
        String localDateTimeStr = formatter.format(localDateTime);

        return localDateTimeStr;
    }


    @Test
    public void hkTest() {
        String json = "{\"code\":\"0\",\"msg\":\"Operation succeeded\",\"data\":{\"pageNo\":1,\"pageSize\":20,\"totalPage\":1,\"total\":1,\"list\":[{\"deviceType\":null,\"regionIndexCode\":\"d2e902df-dadf-4603-a291-16d194032352\",\"collectTime\":\"2023-04-12T11:48:37.000+08:00\",\"deviceIndexCode\":null,\"ip\":\"172.16.1.184\",\"regionName\":\"本部院区\",\"indexCode\":\"6d6c26b409eb4381a8ed2e8fa4e76252\",\"cn\":\"急诊门口\",\"treatyType\":\"1\",\"manufacturer\":null,\"port\":8000,\"online\":1}]}}";
        int onlineCamera = getOnlineCamera(json);
        System.out.println(onlineCamera);


    }

    public int getOnlineCamera(String result) {
        int online = 0;//0:离线 1:在线

        JSONObject jsonObject = JSON.parseObject(result);

        if (jsonObject.getString("code").equals("0")) {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray onlineArray = data.getJSONArray("list");
            for (int j = 0; j < onlineArray.size(); j++) {
                JSONObject object = onlineArray.getJSONObject(j);
                online = object.getIntValue("online");
            }

        }
        return online;
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        // 获取一周的开始日期
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date startDate = calendar.getTime();
        System.out.println("一周的开始日期：" + DateUtil.format(startDate,DatePattern.NORM_DATE_PATTERN));
        // 获取一周的结束日期
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Date endDate = calendar.getTime();
        System.out.println("一周的结束日期：" + DateUtil.format(endDate,DatePattern.NORM_DATE_PATTERN));

        // 获取当天日期
        LocalDate now = LocalDate.now();

        // 当天开始时间
        LocalDateTime todayStart = now.atStartOfDay();
        // 当天结束时间
        LocalDateTime todayEnd = LocalDateTime.of(now, LocalTime.MAX);

        // 周一
        LocalDate monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        // 周日
        LocalDate sunday = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // 本周开始时间
        LocalDateTime weekStart = monday.atStartOfDay();
        // 本周结束时间
        LocalDateTime weekEnd = LocalDateTime.of(sunday, LocalTime.MAX);

        // 本月1号
        LocalDate firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        // 本月最后一天
        LocalDate lastDayOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());

        // 本月1号的开始时间
        LocalDateTime firstDayOfMonthStart = firstDayOfMonth.atStartOfDay();
        // 本月最后一天的最后时间
        LocalDateTime firstDayOfMonthEnd = LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);

        // 今年第一天
        LocalDate beginTime = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
        // 今年最后一天
        LocalDate endTiime = LocalDate.now().with(TemporalAdjusters.lastDayOfYear());

        //获取前一天日期
        LocalDate yesterday2 = LocalDate.now().minusDays(1);

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("当天开始时间 = " + todayStart.format(pattern));
        System.out.println("当天结束时间 = " + todayEnd.format(pattern));
        System.out.println("本周开始时间 = " + weekStart.format(pattern));
        System.out.println("本周结束时间 = " + weekEnd.format(pattern));
        System.out.println("本月开始时间 = " + firstDayOfMonthStart.format(pattern));
        System.out.println("本月结束时间 = " + firstDayOfMonthEnd.format(pattern));

    }
}



