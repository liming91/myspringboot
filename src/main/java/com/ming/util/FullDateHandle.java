package com.ming.util;


import com.ming.entities.CalendarUtil;
import com.ming.entities.DataTrendVO;
import com.ming.entities.TypeVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

public class FullDateHandle {

    public static List bimDataHandle(List<DataTrendVO> list, Integer dateType, String StartTime) {
        String startTime = "";
        String endTime = "";
        // 当前
        LocalDate today = LocalDate.now().minusMonths(0);
        if (dateType == 0) {
            startTime = "00";
            endTime = "24";
        } else if (dateType == 1) {
            // 根据 年 月 获取当月的第一天和最后一天  包含闰年
            int year = Integer.parseInt(StartTime.substring(0, 4));
            int month = Integer.parseInt(StartTime.substring(5, 7));
            startTime = CalendarUtil.getFirstDayOfMonth1(year, month);
            endTime = CalendarUtil.getLastDayOfMonth1(year, month);
        } else if (dateType == 2) {
            int year = Integer.parseInt(StartTime.substring(0, 4));
            startTime = year + "-01";
            endTime = year + "-12";
        } else if (dateType == 3) {
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(StartTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int d = 0;
            if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                d = -6;
            } else {
                d = 2 - cal.get(Calendar.DAY_OF_WEEK);
            }
            cal.add(Calendar.DAY_OF_WEEK, d);
            // 所在周开始日期
            startTime = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            cal.add(Calendar.DAY_OF_WEEK, 6);
            // 所在周结束日期
            endTime = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        }
        // 根据时间区间，得出区间内所有日期
        List<String> dateList = CalendarUtil.queryData(startTime, endTime, dateType);
        boolean exists;
        for (int i = 0; i < dateList.size(); i++) {
            exists = false;
            for (DataTrendVO dataTrendVO : list) {
                //  当dataType  = 0 时不做处理
                if (dateList.get(i).equals(dataTrendVO.getDateTime())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                final String date = dateList.get(i);
                list.add(i, new DataTrendVO(date, 0.00));
            }
        }
        return list;
    }

    /**
     * 根据自定义时间来获取值
     *
     * @param list
     * @param dateType
     * @param StartTime
     * @param
     * @return
     */
    public static List dataHandle(List<DataTrendVO> list, Integer dateType, String StartTime) {
        String startTime = "";
        String endTime = "";

        if (dateType == 0) {
            startTime = "00";
            endTime = "24";
        } else if (dateType == 2) {
            // 根据 年 月 获取当月的第一天和最后一天  包含闰年
            int year = Integer.parseInt(StartTime.substring(0, 4));
            int month = Integer.parseInt(StartTime.substring(5, 7));
            startTime = CalendarUtil.getFirstDayOfMonth1(year, month);
            endTime = CalendarUtil.getLastDayOfMonth1(year, month);
        } else if (dateType == 3) {
            int year = Integer.parseInt(StartTime.substring(0, 4));
            startTime = year + "-01";
            endTime = year + "-12";
        } else if (dateType == 1) {
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(StartTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int d = 0;
            if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                d = -6;
            } else {
                d = 2 - cal.get(Calendar.DAY_OF_WEEK);
            }
            cal.add(Calendar.DAY_OF_WEEK, d);
            // 所在周开始日期
            startTime = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            cal.add(Calendar.DAY_OF_WEEK, 6);
            // 所在周结束日期
            endTime = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        }
        // 根据时间区间，得出区间内所有日期
        List<String> dateList = CalendarUtil.queryData(startTime, endTime, dateType);
        boolean exists;
        for (int i = 0; i < dateList.size(); i++) {
            exists = false;
            for (DataTrendVO dataTrendVO : list) {
                //  当dataType  = 0 时不做处理
                if (dateList.get(i).equals(dataTrendVO.getDateTime())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                final String date = dateList.get(i);
                list.add(i, new DataTrendVO(date, 0.00));
            }
        }
        return list;
    }

    public static List dataHandle(List<DataTrendVO> list, Integer dateType) {
        String startTime = "";
        String endTime = "";
        // 当前
        LocalDate today = LocalDate.now().minusMonths(0);
        if (dateType == 0) {
            startTime = "00";
            endTime = "24";
        } else if (dateType == 1) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
            // 本月的第一天
            startTime = LocalDate.of(today.getYear(), today.getMonth(), 1).format(formatter);
            // 本月的最后一天
            endTime = today.with(TemporalAdjusters.lastDayOfMonth()).format(formatter);
        } else if (dateType == 2) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            // 第一月第一天
            startTime = LocalDate.of(today.getYear(), 1, 1).format(formatter);
            // 最后一个月最后一天
            endTime = LocalDate.of(today.getYear(), 12, 31).format(formatter);
        }
        // 根据时间区间，得出区间内所有日期
        List<String> dateList = CalendarUtil.queryData(startTime, endTime, dateType);
        boolean exists;
        for (int i = 0; i < dateList.size(); i++) {
            exists = false;
            for (DataTrendVO dataTrendVO : list) {
                //  当dataType  = 0 时不做处理
                if (dataTrendVO != null) {
                    if (dateList.get(i).equals(dataTrendVO.getDateTime())) {
                        exists = true;
                        break;
                    }
                }
            }
            if (!exists) {
                final String date = dateList.get(i);
                list.add(i, new DataTrendVO(date, 0.00));
            }
        }
        List<DataTrendVO> collect = list.stream().sorted(Comparator.comparing(DataTrendVO::getDateTime)).collect(Collectors.toList());
        return collect;
    }

    public static List dataHandleType(List<TypeVO> list, Integer dateType, String StartTime) {
        String startTime = "";
        String endTime = "";

        if (dateType == 0) {
            startTime = "00";
            endTime = "24";
        } else if (dateType == 2) {
            // 根据 年 月 获取当月的第一天和最后一天  包含闰年
            int year = Integer.parseInt(StartTime.substring(0, 4));
            int month = Integer.parseInt(StartTime.substring(5, 7));
            startTime = CalendarUtil.getFirstDayOfMonth1(year, month);
            endTime = CalendarUtil.getLastDayOfMonth1(year, month);
        } else if (dateType == 3) {
            int year = Integer.parseInt(StartTime.substring(0, 4));
            startTime = year + "-01";
            endTime = year + "-12";
        } else if (dateType == 1) {

            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(StartTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int d = 0;
            if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                d = -6;
            } else {
                d = 2 - cal.get(Calendar.DAY_OF_WEEK);
            }
            cal.add(Calendar.DAY_OF_WEEK, d);
            // 所在周开始日期
            startTime = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            cal.add(Calendar.DAY_OF_WEEK, 6);
            // 所在周结束日期
            endTime = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        }
        // 根据时间区间，得出区间内所有日期
        List<String> dateList = CalendarUtil.queryData(startTime, endTime, dateType);
        boolean exists;
        for (int i = 0; i < dateList.size(); i++) {
            exists = false;
            for (TypeVO typeVo : list) {
                //  当dataType  = 0 时不做处理
                if (dateList.get(i).equals(typeVo.getTypeName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                final String date = dateList.get(i);
                list.add(i, new TypeVO(date, 0));
            }
        }
        return list;
    }


    public static List<String> getWeeksByYear(int year) {
        if (year < 1900 || year > 9999) {
            throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
        }
//实现思路，首先计算当年有多少个周，然后找到每个周的开始日期和结束日期
        Calendar calendar = new GregorianCalendar();
// // 在具有默认语言环境的默认时区内使用当前时间构造一个默认的 GregorianCalendar。
        calendar.setFirstDayOfWeek(Calendar.MONDAY); //设置每周的第一天为星期一
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //每周从周一开始
// 上面两句代码配合，才能实现，每年度的第一个周，是包含第一个星期一的那个周。
        calendar.setMinimalDaysInFirstWeek(7); //设置每周最少为7天
        calendar.set(Calendar.YEAR, year); // 设置年度为指定的年get
// //首先计算当年有多少个周,每年都至少有52个周，个别年度有53个周

        // 取值当年有多少周
        int weekYear = calendar.getWeeksInWeekYear();
        // System.out.println(year+"共有"+weeks+"个周");
        List<String> result = new ArrayList<String>(weekYear);
        for (int i = 1; i <= weekYear; i++) {
            String localYear = String.valueOf(year);
            if (i < 10) {
                result.add(localYear + "-" + "0" + i);
            } else {
                result.add(localYear + "-" + i);
            }
        }
        return result;
    }


}
