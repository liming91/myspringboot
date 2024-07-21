package com.ming.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * @author cdd
 * @Date 2020/10/17 14:05
 * @Version 1.0
 */
public class WeekToDayUtil {

    /**
     * 当前周的第最后一天
     * @param year
     * @param week
     */
    public static String weekToDayFormate(int year, int week){
        Calendar calendar = Calendar.getInstance();
        // ①.设置该年份的开始日期：第一个月的第一天
        calendar.set(year,0,1);
        // ②.计算出第一周还剩几天：+1是因为1号是1天
        int dayOfWeek = 7 - calendar.get(Calendar.DAY_OF_WEEK) + 1;
        // ③.周数减去第一周再减去要得到的周
        week = week - 2;
        // ④.计算起止日期
        calendar.add(Calendar.DAY_OF_YEAR,week * 7 + dayOfWeek);
        calendar.add(Calendar.DAY_OF_YEAR, 6);
        String result = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        return result;
    }

    /**
     * 当前周的第一天
     * @param year
     * @param week
     */
    public static String weekToDayFisrtFormate(int year, int week){
        Calendar calendar = Calendar.getInstance();
        // ①.设置该年份的开始日期：第一个月的第一天
        calendar.set(year,0,1);
        // ②.计算出第一周还剩几天：+1是因为1号是1天
        int dayOfWeek = 7 - calendar.get(Calendar.DAY_OF_WEEK) + 1;
        // ③.周数减去第一周再减去要得到的周
        week = week - 2;
        // ④.计算起止日期
        calendar.add(Calendar.DAY_OF_YEAR,week * 7 + dayOfWeek);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String result = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        return result;
    }


    public static  void getWeekStar(){
        Calendar calendar = Calendar.getInstance();
        // 获取一周的开始日期
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date startDate = calendar.getTime();
        System.out.println("一周的开始日期：" + DateUtil.format(startDate, DatePattern.NORM_DATE_PATTERN));
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



    public static String weekStartTime() {
        // 获取当天日期
        LocalDate now = LocalDate.now();
        // 周一
        LocalDate monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        // 周日
        LocalDate sunday = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        // 本周开始时间
        return DateUtil.format(monday.atStartOfDay(), DatePattern.NORM_DATE_PATTERN);
    }

    /**
     * 本周结束时间
     *
     * @return
     */
    public static String weekEndTime() {
        // 获取当天日期
        LocalDate now = LocalDate.now();
        // 周一
        LocalDate monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        // 周日
        LocalDate sunday = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        // 本周开始时间
        return DateUtil.format(sunday.atStartOfDay(), DatePattern.NORM_DATE_PATTERN);
    }

    public static void main(String[] args) {
        String s = weekStartTime();
        String s1 = weekEndTime();
        System.out.println(s);
        System.out.println(s1);
    }
}
