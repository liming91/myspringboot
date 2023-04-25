package com.ming.entities;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.ming.entities.VO.DataTrendVO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 12099
 */
public class CalendarUtil {

    /**
     * 给定开始和结束时间，遍历之间的所有日期
     *
     * @param startAt 开始时间，例：2019-04-04
     * @param endAt   结束时间，例：2019-04-11
     * @return 返回日期数组
     */
    public static List<String> queryData(String startAt, String endAt, Integer dateType) {
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat dateFormatDay = new SimpleDateFormat("MM-dd");
        SimpleDateFormat dateFormatHour = new SimpleDateFormat("HH24");
        SimpleDateFormat dateFormatWeek = new SimpleDateFormat("yyyy-WW");
        List<String> dates = new ArrayList<>();
        try {
            Date startDate = null;
            Date endDate = null;
            if (dateType == 0) {
                dates.addAll(queryDataHour(startDate, endDate));
            } else if (dateType == 1) {
                List<String> strings = queryDataDayString(startAt, endAt);
                for (String string : strings) {
                    dates.add(string.substring(5, 10));
                }
            } else if (dateType == 2) {
                startDate = dateFormatMonth.parse(startAt);
                endDate = dateFormatMonth.parse(endAt);
                dates.addAll(queryDataMonth(startDate, endDate));
            } else if (dateType == 4) {
                List<String> strings = queryDataDayString(startAt, endAt);
                for (String string : strings) {
                    dates.add(string);
                }
            }else  if(dateType==3){
                for (int i = 0; i < 24; i++) {
                    if (i < 10) {
                        dates.add("0" + i + ":00");
                        dates.add("0" + i + ":15");
                        dates.add("0" + i + ":30");
                        dates.add("0" + i + ":45");
                    } else {
                        dates.add(i + ":00");
                        dates.add(i + ":15");
                        dates.add(i + ":30");
                        dates.add(i + ":45");
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates;
    }

    /***
     *
     * //                startDate = dateFormatDay.parse(startAt);
     * //                endDate = dateFormatDay.parse(endAt);
     * //                System.out.println(startDate);
     * //                System.out.println(endDate);
     * ////                dates.addAll(queryDataDay(startDate, endDate));
     *        //根据时间获取当年有多少周
     * //                    int year = Integer.parseInt(startAt);
     * //                    //实现思路，首先计算当年有多少个周，然后找到每个周的开始日期和结束日期
     * //                    Calendar calendar = new GregorianCalendar();
     * //                    // // 在具有默认语言环境的默认时区内使用当前时间构造一个默认的 GregorianCalendar。
     * //                    calendar.setFirstDayOfWeek(Calendar.MONDAY); //设置每周的第一天为星期一
     * //                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //每周从周一开始
     * //                    // 上面两句代码配合，才能实现，每年度的第一个周，是包含第一个星期一的那个周。
     * //                    calendar.setMinimalDaysInFirstWeek(7); //设置每周最少为7天
     * //                calendar.set(Calendar.YEAR, year); // 设置年度为指定的年get
     * //                    // //首先计算当年有多少个周,每年都至少有52个周，个别年度有53个周
     * //                    // 取值当年有多少周
     * //                    int weekYear = calendar.getWeeksInWeekYear();
     * //                    for(int i=1;i<=weekYear;i++){
     * //                        String localYear = String.valueOf(year);
     * //                        if (i<10){
     * //                            dates.add(localYear+"-"+"0"+i);
     * //                        }else {
     * //                            dates.add(localYear+"-"+i);
     * //                        }
     * //                    }
     */

    /**
     * 给定开始和结束时间，遍历之间的所有日期
     *
     * @param startAt 开始时间，例：2019-04-04
     * @param endAt   结束时间，例：2019-04-11
     * @return 返回日期数组
     */
    public static List<String> queryDataChg(String startAt, String endAt, Integer dateType) {
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat dateFormatDay = new SimpleDateFormat("MM-dd");
        SimpleDateFormat dateFormatHour = new SimpleDateFormat("HH24");
        List<String> dates = new ArrayList<>();
        try {
            Date startDate = null;
            Date endDate = null;
            if (dateType == 0) {
                dates.addAll(queryDataHour(startDate, endDate));
            } else if (dateType == 1) {
                startDate = dateFormatDay.parse(startAt);
                endDate = dateFormatDay.parse(endAt);
                dates.addAll(queryDataDay(startDate, endDate));
            } else if (dateType == 2) {
                startDate = dateFormatMonth.parse(startAt);
                endDate = dateFormatMonth.parse(endAt);
                dates.addAll(queryDataMonth(startDate, endDate));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates;
    }

    /**
     * 给定开始和结束时间，遍历之间的所有日期
     *
     * @param startAt 开始时间，例：2019-04-04
     * @param endAt   结束时间，例：2019-04-11
     * @return 返回日期数组
     */
    public static List<String> queryDataDay(Date startAt, Date endAt) {
        SimpleDateFormat dateFormatDay = new SimpleDateFormat("MM-dd");
        List<String> dates = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(startAt);
        Calendar end = Calendar.getInstance();
        end.setTime(endAt);
        while (start.before(end) || start.equals(end)) {
            dates.add(dateFormatDay.format(start.getTime()));
            start.add(Calendar.DAY_OF_YEAR, 1);
        }
        return dates;
    }

    /**
     * 两个日期之间的时间
     *
     * @param startAt 开始时间，例：2019-04-04
     * @param endAt   结束时间，例：2019-04-11
     * @return
     * @throws ParseException
     */
    public static List<String> queryDataDayString(String startAt, String endAt) throws ParseException {
        // 返回的日期集合
        List<String> days = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startAtDate = dateFormat.parse(startAt);
        Date endAtDate = dateFormat.parse(endAt);
        try {
            Calendar tempStart = Calendar.getInstance();
            tempStart.add(Calendar.YEAR, -3);
            // 年份减3
            // 可以指定时间区间
            tempStart.setTime(startAtDate);
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(endAtDate);
            tempEnd.add(Calendar.DATE, +1);
            // 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 给定开始和结束时间，遍历之间的所有日期
     *
     * @param startAt 开始时间，例：00
     * @param endAt   结束时间，例：24
     * @return 返回日期数组
     */
    public static List<String> queryDataHour(Date startAt, Date endAt) {
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                dates.add("0" + i);
            } else {
                dates.add(String.valueOf(i));
            }
        }
        return dates;
    }

    /**
     * 给定开始和结束时间，遍历之间的所有日期
     *
     * @param startAt 开始时间，例：2019-04-04
     * @param endAt   结束时间，例：2019-04-11
     * @return 返回日期数组
     */
    public static List<String> queryDataMonth(Date startAt, Date endAt) {
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
        List<String> dates = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(startAt);
        Calendar end = Calendar.getInstance();
        end.setTime(endAt);
        while (start.before(end) || start.equals(end)) {
            dates.add(dateFormatMonth.format(start.getTime()));
            start.add(Calendar.MONDAY, 1);
        }
        return dates;
    }

    /**
     * 获取指定年月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth1(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取指定年月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth1(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }


    public static List<DataTrendVO> getTheCurrentTime(List<DataTrendVO> DataTrendVOList) {
        String queryTime;
        int hour = LocalTime.now().getHour();
        if (hour < 10) {
            queryTime = "0" + hour;
        } else {
            queryTime = String.valueOf(hour);
        }
        System.out.println(hour);
        List<DataTrendVO> result = new ArrayList<>();
        Integer finalQueryTime = Integer.valueOf(queryTime);
        DataTrendVOList.stream().forEach(x -> {

            if (Integer.parseInt(x.getDateTime()) < finalQueryTime) {
                result.add(x);
            }
        });
        return result;
    }


    /**
     * 根据传入时间截取时间段  2020-01-01
     *
     * @param startTime
     * @param dateType
     * @return
     */
    public static String formatDay(String startTime, Integer dateType) {
        switch (dateType) {
            case 0:
                return startTime;
            case 1:
                return startTime.substring(0, 7);
            case 2:
                return startTime.substring(0, 4);
        }
        return startTime;
    }

    /**
     * 日期格式化工具   根据日期类型的不同值 获取传入时间点的
     *
     * @param startTime 格式要求 2020-01-01
     * @param endTime   格式要求 2020-01-01
     * @param dateType  时间类型  0 day:2020-01-01   1 month:01-01   2 year:2020
     * @return
     */
    public static List<String> formatDay(String startTime, String endTime, Integer dateType) {
        List<String> times = new ArrayList<>();
        if (dateType == 0) {
            times.add(startTime);
            times.add(endTime);
        } else if (dateType == 1) {
            times.add(startTime.substring(0, 7));
            times.add(startTime.substring(0, 7));
        } else if (dateType == 2) {
            int startTimeMonth = Integer.parseInt(startTime.substring(0, 4));
            ;
            times.add(String.valueOf(startTimeMonth));
            times.add(String.valueOf(startTimeMonth));
        }
        System.out.println(times);
        return times;

    }

    public static List<String> getTimeList(String startTime, String endTime, Integer timeType) {
        List<String> dates = new ArrayList<>();
        try {
            if (timeType == 0) {
                for (int i = 0; i < 24; i++) {
                    if (i < 10) {
                        dates.add("0" + i);
                    } else {
                        dates.add(String.valueOf(i));
                    }
                }
            } else if (timeType == 1) {
                List<String> strings = queryDataDayString(startTime, endTime);
                for (String string : strings) {
                    dates.add(string.substring(5, string.length()));
                }
            } else if (timeType == 2) {
                List<String> strings = queryDataMonth(DateUtil.parse(startTime), DateUtil.parse(endTime));
                for (String string : strings) {
                    dates.add(string.substring(0, 7));
                }
            } else if (timeType == 3) {
                for (int i = 0; i < 24; i++) {
                    if (i < 10) {
                        dates.add("0" + i + ":00");
                        dates.add("0" + i + ":15");
                        dates.add("0" + i + ":30");
                        dates.add("0" + i + ":45");
                    } else {
                        dates.add(i + ":00");
                        dates.add(i + ":15");
                        dates.add(i + ":30");
                        dates.add(i + ":45");
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates;
    }


    public static void main(String[] args) {
        List<String> timeList = getTimeList("2023-01-01 21:00:00", "2023-01-01 21:00:00", 0);
        System.out.println(timeList.size());
        System.out.println(JSON.toJSONString(timeList));
    }

}
