package com.ming.util;

import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public static void main(String[] args) {
        String s = weekToDayFisrtFormate(2020, 43);
        System.out.println(s);
        int i1 = DateUtil.weekOfYear(DateUtil.parse(s));
        System.out.println(i1);
    }
}
