package com.ming;

import cn.hutool.core.date.*;
import cn.hutool.core.text.StrPool;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.ming.annotation.Log;
import com.ming.entities.VO.SendEleWarningVo;
import com.ming.spring.SpringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author liming
 * @Date 2023/1/12 11:48
 */
public class DateTest {


    @Test
    public void date1() {

        String day = "2023-01-12";
        String format = DateUtil.format(DateUtil.offsetDay(new Date(), -6), DatePattern.NORM_DATE_PATTERN);
        System.out.println(format);

        String format1 = DateUtil.format(DateUtil.offsetMonth(DateUtil.parse(day), 0), DatePattern.NORM_MONTH_PATTERN);
        System.out.println(format1);


        DateTime dateTime = DateUtil.offsetMonth(DateUtil.parse(day), -12);
        String lastYear = DateUtil.format(dateTime, "yyyy");
        String mm = DateUtil.format(DateUtil.parse(day), "MM");
        String lastYearMonth = lastYear + "-" + mm;
        System.out.println(lastYearMonth);


        String year = DateUtil.format(DateUtil.parse(day), "yyyy");
        System.out.println(year);
        String lastYear1 = getLastYear(day);
        System.out.println(lastYear);
    }

    /**
     * 根据时间获取去年
     *
     * @return
     */
    public static String getLastYear(String time) {
        DateTime dateTime = DateUtil.offsetMonth(DateUtil.parse(time), -12);
        return DateUtil.format(dateTime, "yyyy");
    }

    /**
     * Date、long、Calendar之间的相互转换
     */
    public static String getStr() {
        //当前时间
        Date date = DateUtil.date();
        //当前时间
        Date date2 = DateUtil.date(Calendar.getInstance());
        //当前时间
        Date date3 = DateUtil.date(System.currentTimeMillis());
        //当前时间字符串，格式：yyyy-MM-dd HH:mm:ss
        String now = DateUtil.now();
        //当前日期字符串，格式：yyyy-MM-dd
        return DateUtil.today();
    }


    /**
     * 判断format(2023-09-01 10:59:41)是否在系统时间之后
     * 返回true表示是format在系统时间(2023-09-04 10:59:41)之后
     *
     * @param format
     * @return
     */
    public static boolean after(Date format) {
        Date now = DateUtil.date();
//        DateTime newDate2 = DateUtil.offsetDay(new Date(), 3);
//        boolean after = after(DateUtil.parse(DateUtil.format(newDate2,DatePattern.NORM_DATETIME_PATTERN)));
//        System.out.println(after);
        return now.after(format);
    }

    @Test
    public void test2() {
        String date1 = "2023-09-01 16:10:00";
        String date2 = "2023-09-01 16:12:00";
        Date dateStr = DateUtil.parse(date1);
        Date dateEnd = DateUtil.parse(date2);
        long between = DateUtil.between(dateStr, dateEnd, DateUnit.MINUTE);
        System.out.println(between);
    }


    @Test
    public void test7() {
        String format = DateUtil.format(DateUtil.offsetMonth(DateUtil.yesterday(), -12),
                DatePattern.NORM_DATE_PATTERN);
        System.out.println(format);
    }


    @Test
    public void test3() {
        //date1.before(date2) 的返回值为 true，表示 date1 早于 date2。而 date2.after(date1) 的返回值为 true，表示 date2 晚于 date1。

        //因此，before 方法表示日期是否早于另一个日期，after 方法表示日期是否晚于另一个日期。这两个方法的返回值都是布尔值，因此您可以使用它们来编写逻辑判断语句。
        Date date1 = new Date(System.currentTimeMillis());
        Date date2 = new Date(System.currentTimeMillis() + 1000000000);

        System.out.println("date1 is before date2: " + date1.before(date2));
        System.out.println("date2 is after date1: " + date2.after(date1));
        Date date = new Date();
        String a = "2029-12-31 12:22:00";
        System.out.println(date.after(DateUtil.parse(a, DatePattern.NORM_DATETIME_PATTERN)));
    }

    @Test
    public void test4() {
        //6天22小时40分
        String formatBetween = DateUtil.formatBetween(DateUtil.parse("2023-11-20 10:24:09"), DateUtil.parse("2023-11-13 11:43:23"), BetweenFormatter.Level.MINUTE);
        System.out.println(formatBetween);
    }

    private static final double[] BEAUFORT_SCALE = {0.3, 1.6, 3.4, 5.5, 7.9, 10.8, 13.9, 17.2, 20.8, 24.5, 28.5, 32.7, 36.9};
    private static final String[] DESCRIPTIONS = {"Calm", "Light air", "Light breeze", "Gentle breeze", "Moderate breeze", "Fresh breeze", "Strong breeze", "Near gale", "Gale", "Strong gale", "Storm", "Violent storm", "Hurricane"};

    public static int calculateWindPower(double windSpeed) {
        for (int i = 0; i < BEAUFORT_SCALE.length; i++) {
            if (windSpeed <= BEAUFORT_SCALE[i]) {
                return i;
            }
        }
        return BEAUFORT_SCALE.length - 1;
    }

    public static String getWindPowerDescription(int windPower) {
        return DESCRIPTIONS[windPower];
    }


    public static void main(String[] args) throws InterruptedException {
//        2024-07-15 17:30:00	2024-07-15 18:45:00  1小时15分钟
//        2024-07-15 20:30:00	2024-07-15 21:45:00  1小时15分钟
//        2024-07-15 22:30:00	2024-07-16 01:00:00  2小时30分钟
//        2024-07-16 05:30:00	2024-07-16 07:15:00  1小时45分钟
//        2024-07-16 12:45:00	2024-07-16 14:52:49  2小时7分钟
        String starStr = "2024-07-16 12:45:00";
        String endStr = "2024-07-16 14:52:49";
        List<SendEleWarningVo> list = new ArrayList<>();
        SendEleWarningVo sendEleWarningVo1 = new SendEleWarningVo();
        sendEleWarningVo1.setEnterCode("7106");
        sendEleWarningVo1.setLevels("3");
        sendEleWarningVo1.setAreaName("王益区");
        sendEleWarningVo1.setEnterName("陕西铜川长兴支撑剂有限责任公司");
        sendEleWarningVo1.setStartTime("2024-07-15 17:30:00");
        sendEleWarningVo1.setEndTime("2024-07-15 18:45:00");
        sendEleWarningVo1.setStatus("2");
        sendEleWarningVo1.setContent("平台报警治污设备除尘风机01停运");
        list.add(sendEleWarningVo1);
        SendEleWarningVo sendEleWarningVo2 = new SendEleWarningVo();
        sendEleWarningVo2.setEnterCode("7106");
        sendEleWarningVo2.setLevels("3");
        sendEleWarningVo2.setAreaName("王益区");
        sendEleWarningVo2.setEnterName("陕西铜川长兴支撑剂有限责任公司");
        sendEleWarningVo2.setStartTime("2024-07-15 20:30:00");
        sendEleWarningVo2.setEndTime("2024-07-15 21:45:00");
        sendEleWarningVo2.setStatus("2");
        sendEleWarningVo2.setContent("平台报警治污设备除尘风机01停运");
        list.add(sendEleWarningVo1);
        SendEleWarningVo sendEleWarningVo3 = new SendEleWarningVo();
        sendEleWarningVo3.setEnterCode("7106");
        sendEleWarningVo3.setLevels("2");
        sendEleWarningVo3.setAreaName("王益区");
        sendEleWarningVo3.setEnterName("陕西铜川长兴支撑剂有限责任公司");
        sendEleWarningVo3.setStartTime("2024-07-15 22:30:00");
        sendEleWarningVo3.setEndTime("2024-07-16 01:00:00");
        sendEleWarningVo3.setStatus("2");
        sendEleWarningVo3.setContent("平台报警治污设备除尘风机01停运");
        list.add(sendEleWarningVo3);

        SendEleWarningVo sendEleWarningVo4 = new SendEleWarningVo();
        sendEleWarningVo4.setEnterCode("7106");
        sendEleWarningVo4.setLevels("3");
        sendEleWarningVo4.setAreaName("王益区");
        sendEleWarningVo4.setEnterName("陕西铜川长兴支撑剂有限责任公司");
        sendEleWarningVo4.setStartTime("2024-07-16 05:30:00");
        sendEleWarningVo4.setEndTime("2024-07-16 07:15:00");
        sendEleWarningVo4.setStatus("2");
        sendEleWarningVo4.setContent("平台报警治污设备除尘风机01停运");
        list.add(sendEleWarningVo4);


        SendEleWarningVo sendEleWarningVo5 = new SendEleWarningVo();
        sendEleWarningVo5.setEnterCode("7141");
        sendEleWarningVo5.setLevels("3");
        sendEleWarningVo5.setAreaName("铜川新区");
        sendEleWarningVo5.setEnterName("陕西大唐合力电缆有限公司");
        sendEleWarningVo5.setStartTime("2024-07-16 12:45:00");
        sendEleWarningVo5.setEndTime("2024-07-16 14:45:00");
        sendEleWarningVo5.setStatus("3");
        sendEleWarningVo5.setContent("平台报警治污设备电缆产线治理01停运");
        list.add(sendEleWarningVo5);

        //耀州区孙塬镇  铜川市耀州区鑫辕建材有限公司 破碎房 平台报警治污设备收尘风机03停运 报警开始时间2024-03-29 01:15:00 持续时间1小时
        Map<String, List<SendEleWarningVo>> collect = list.stream().collect(Collectors.groupingBy(SendEleWarningVo::getEnterName));
        for (Map.Entry<String, List<SendEleWarningVo>> entry : collect.entrySet()) {
            String key = entry.getKey();
            long fen = 0;
            List<SendEleWarningVo> value = entry.getValue();
            for (SendEleWarningVo sendEleWarningVo : value) {
                if (sendEleWarningVo.getEnterName().equals(key)) {
                    fen += DateUtil.between(DateUtil.parse(sendEleWarningVo.getEndTime()), DateUtil.parse(sendEleWarningVo.getStartTime()), DateUnit.MINUTE);
                }
            }
            String content = value.stream().map(SendEleWarningVo::getContent).findFirst().orElse(null);
            String areaName = value.stream().map(SendEleWarningVo::getAreaName).findFirst().orElse(null);
            String StartTime = value.stream().map(SendEleWarningVo::getStartTime).findFirst().orElse(null);
            System.out.println(areaName + StrPool.C_SPACE + key + StringUtils.SPACE + content + StringUtils.SPACE + "报警开始时间：" + StartTime + StringUtils.SPACE + "持续时间" + com.ming.util.DateUtils.minTime(fen));
        }


    }

    public static double getWindSpeed(String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return 0.0;
        }
        // 定义一个匹配数字的正则表达式
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)"); // 匹配浮点数或整数
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        } else {
            return 0.0;
        }
    }


    public static List<String> getSevenDate(int day) {
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < day; i++) {
            Date date = DateUtils.addDays(new Date(), -i);
            dateList.add(DateUtil.format(date, "MM.dd"));
        }
        Collections.sort(dateList);
        return dateList;
    }

}
