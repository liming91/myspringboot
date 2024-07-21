package com.ming.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @Author liming
 * @Date 2024/7/17 17:46
 */
public class LastDateUtils {

    /**
     *
     * @param type 0:最近7天 1:最近一月 2:本年
     * @return
     */
    public static String getTime(int type) {
        String time = null;
        if (type == 0) {
            time = DateUtil.format(DateUtil.offsetDay(new Date(), -6), DatePattern.NORM_DATE_PATTERN);
        }
        if (type == 1) {
            time = DateUtil.format(DateUtil.offsetDay(new Date(), -29), DatePattern.NORM_DATE_PATTERN);
        }
        if (type == 3) {
            time = DateUtil.format(new Date(), "yyyy");
        }
        return time;
    }


    public static void main(String[] args) {
        String time = getTime(1);
        System.out.println(time);
    }
}
