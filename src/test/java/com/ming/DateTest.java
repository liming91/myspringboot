package com.ming;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author liming
 * @Date 2023/1/12 11:48
 */
public class DateTest {


    @Test
    public void date1() {

        String day = "2023-01-12";
        String format = DateUtil.format(DateUtil.offsetDay(DateUtil.parse(day), -1), DatePattern.NORM_DATE_PATTERN);
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
     * 判断format(2023-09-01 10:59:41)是否在系统时间之后
     *  返回true表示是format在系统时间(2023-09-04 10:59:41)之后
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
    public void test3() {
        //date1.before(date2) 的返回值为 true，表示 date1 早于 date2。而 date2.after(date1) 的返回值为 true，表示 date2 晚于 date1。

        //因此，before 方法表示日期是否早于另一个日期，after 方法表示日期是否晚于另一个日期。这两个方法的返回值都是布尔值，因此您可以使用它们来编写逻辑判断语句。
        Date date1 = new Date(System.currentTimeMillis());
        Date date2 = new Date(System.currentTimeMillis() + 1000000000);

        System.out.println("date1 is before date2: " + date1.before(date2));
        System.out.println("date2 is after date1: " + date2.after(date1));

    }

    public static void main(String[] args) throws InterruptedException {
        Date date = new Date();
        System.out.println(DateUtil.format(date, DatePattern.NORM_DATETIME_PATTERN));
        DateTime newDate1 = DateUtil.offsetDay(new Date(), +1);
        System.out.println(DateUtil.format(newDate1, DatePattern.NORM_DATETIME_PATTERN));
        boolean before = newDate1.before(date);
        System.out.println(before);
        boolean a = newDate1.after(date);
        System.out.println(a);
    }


}
