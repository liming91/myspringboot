package com.ming.enums;

import cn.hutool.core.date.DatePattern;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author 12099
 */

@AllArgsConstructor
public enum DateTypeEnum {
    //  日期格式化  前端日期格式化 受这个影响
//    day(0, "HH24", "yyyy-MM-dd","日"),
//    week(4, "yyyy-MM-dd", "yyyy-iw","周"),
//    month(1, "MM-dd", "yyyy-MM","月"),
//    quarter(3, "yyyy-Q", "yyyy-Q","季度"),
//    year(2,  "yyyy-MM", "yyyy","年");
    day(0, "%H", "%Y-%m-%d","日"),
    month(1, "%m-%d", "%Y-%m","月"),
    year(2,  "%Y-%m", "%Y","年"),
    fen(3,  "%H:%i", "%Y-%m-%d","时分");



    private Integer index;
    private String resDateFormatter;
    private String queryDateFormatter;
    private  String node;

    private Integer index(){
        return this.index;
    }
    private String resDateFormatter() { return this.resDateFormatter; }
    private String queryDateFormatter() { return this.queryDateFormatter; }
    private String node() { return this.node; }

    public static String getResDateFormatterEnum (Integer index) {
        DateTypeEnum[] dateTypeEnums = values();
        for (DateTypeEnum dateTypeEnum : dateTypeEnums) {
            if (dateTypeEnum.index().equals(index)) {
                return dateTypeEnum.resDateFormatter();
            }
        }
        return null;
    }

    public static String getQueryDateFormatterEnum (Integer index) {
        DateTypeEnum[] dateTypeEnums = values();
        for (DateTypeEnum dateTypeEnum : dateTypeEnums) {
            if (dateTypeEnum.index().equals(index)) {
                return dateTypeEnum.queryDateFormatter();
            }
        }
        return null;
    }
    public static String getDateByType(int type){
        switch (type){
            case 0:
                return LocalDate.now().format(DatePattern.NORM_DATE_FORMATTER);
            case 1:
                return LocalDate.now().format(DatePattern.NORM_MONTH_FORMATTER);
            case 2:
                return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        }
        return null;
    }

}
