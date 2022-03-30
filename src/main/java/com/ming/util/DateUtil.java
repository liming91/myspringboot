package com.ming.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private static final long nd = 1000 * 24 * 60 * 60;
    private static final long nh = 1000 * 60 * 60;
    private static final long nm = 1000 * 60;
    public static String DEFAULT_FORMAT = "yyyy-MM-dd";
    /**
     * 获取某季度的开始日期 季度一年四季， 第一季度：2月-4月， 第二季度：5月-7月， 第三季度：8月-10月， 第四季度：11月-1月
     *
     * @param offset 0本季度，1下个季度，-1上个季度，依次类推
     * @return
     */
    public static String quarterStart(int offset) {
        final LocalDate date = LocalDate.now().plusMonths(offset * 3);
        int year = date.getYear();
        int month = date.getMonth().getValue(); // 当月
        int quarter = 1;
        int start = 0;
        if (month >= 2 && month <= 4) { // 第一季度
            start = 2;
            quarter = 1;
        } else if (month >= 5 && month <= 7) { // 第二季度
            start = 5;
            quarter = 2;
        } else if (month >= 8 && month <= 10) { // 第三季度
            start = 8;
            quarter = 3;
        } else if ((month >= 11 && month <= 12)) { // 第四季度
            start = 11;
            quarter = 4;
        } else if (month == 1) { // 第四季度
            start = 11;
            month = 13;
            quarter = 4;
        }
        String quarterStr = year + "-" + quarter;
        return quarterStr;
    }
    public static String getLastDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay=0;
        //2月的平年瑞年天数
        if(month==2) {
            // 这个api在计算2020年2月的过程中有问题
            lastDay = calendar.getLeastMaximum(Calendar.DAY_OF_MONTH);
        }else {
            lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // 设置日历中月份的最大天数
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDays = sdf.format(calendar.getTime())+" 23:59:59";
        return lastDays;
    }



    /**
     * 格式化日期
     * @param date 日期对象
     * @return String 日期字符串
     */
    public static String formatDate(Date date){
        SimpleDateFormat f = new SimpleDateFormat(DEFAULT_FORMAT);
        String sDate = f.format(date);
        return sDate;
    }

    /**
     * 获取去年的第一天和最后一天
     * @return Date
     */
    public static Date getLastYearFirst(){
        DateTimeFormatter dailyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Year years = Year.now().minusYears(1);//当前年去掉minusYears(1)
        String start = years.atDay(1).format(dailyFormatter);
        System.out.println(start);
        String end = years.atMonth(12).atEndOfMonth().format(dailyFormatter);
        return null;
    }


    /**
     * 计算两个时间段时间差，精确到秒
     *
     * @param startTime 2019-04-10 17:16:11
     * @param endTime   2019-04-10 17:28:17
     * @return
     */
    public static String computationTime(String startTime, String endTime) {
        String str = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date endDate = sdf.parse(endTime);
            Date strDate = sdf.parse(startTime);
            long diff = endDate.getTime() - strDate.getTime();
            long day = diff / nd;
            long hour = diff % nd / nh;
            long min = diff % nd % nh / nm;
            long sec = diff % nd % nh % nm / 1000;//秒
            if (day == 0) {
                str = hour + "小时" + min + "分钟";
            } else if (hour == 0) {
                str = min + "分钟";
            } else {
                str = day + "天" + hour + "小时" + min + "分钟";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取系统日期
     *
     * @param format
     * @return
     */
    public static String getNowDate(String format) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        String nowStr = now.format(df);
        return nowStr;
    }

    /**
     * 获取昨天的日期
     * @param format
     * @return
     */
    public static String getYesterday(String format){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date d=cal.getTime();
        SimpleDateFormat sp=new SimpleDateFormat(format);
        String yesterday=sp.format(d);
        return yesterday;
    }



    /**
     * 获取当前月第一天
     *
     * @return
     */
    public static String getCurrMonthFistDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        return first;
    }

    /**
     * 获取当前月最后一天
     *
     * @return
     */
    public static String getCurrMonthLastDay() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        return last;
    }

    /**
     * 返回时间格式如：2020-02-17 00:00:00
     * @param time
     * @return
     */
    public static String getStartOfDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

}
