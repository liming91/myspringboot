package com.ming;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.ming.bean.User;
import com.ming.entities.*;
import lombok.Builder;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            int t = getSum(n-1);
            System.out.println("n==:"+n);
            System.out.println("t==:"+t);
            return n + t;
        }
    }



    public static void main(String[] args) {


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
}



