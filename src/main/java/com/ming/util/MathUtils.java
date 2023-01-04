package com.ming.util;


import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.time.LocalDate;

/**
 * @param {params}
 * @author hpz
 * @date 2019/10/29 10:03
 * @return ${return}
 */
public class MathUtils {

    /**
     * @author fuxuan
     * 元转万元且保留两位小数并四舍五入
     */
    public static String getNumberWan(Double value,String format) {
        BigDecimal bigDecimal = new BigDecimal(value);
        // 转换为万元（除以10000）
        BigDecimal decimal = bigDecimal.divide(new BigDecimal("10000"));
        // 保留小数
        DecimalFormat formater = new DecimalFormat(format);
        // 四舍五入
        formater.setRoundingMode(RoundingMode.HALF_UP);

        // 格式化完成之后得出结果
        String rs = formater.format(decimal);
        return rs;
    }

    public static double formatDigit(Double num, int decimalPlace) {
        DecimalFormat fm = null;
        switch (decimalPlace) {
            case 0:
                fm = new DecimalFormat("##");
                break;
            case 1:
                fm = new DecimalFormat("##.#");
                break;
            case 2:
                fm = new DecimalFormat("##.##");
                break;
            case 3:
                fm = new DecimalFormat("##.###");
                break;
            case 4:
                fm = new DecimalFormat("##.####");
                break;
            default:
                break;
        }

        if (fm == null) {
            return num;
        }

        StringBuffer sbf = new StringBuffer();
        fm.format(num, sbf, new FieldPosition(java.text.NumberFormat.FRACTION_FIELD));
        return Double.parseDouble(sbf.toString());
    }

    /**
     * String  类无理数  保留两位小数
     *
     * @return
     */

    public static Double twoDecimalPlaces(Double number){
        if ( number == null){
            return 0.0;
        }else {
            return BigDecimal.valueOf(number).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue(); }
    }

    /**
     *  如果是数字 保留两位小数
     * @param number
     * @return
     */
    public static Double twoDecimalPlacesObject(String number){
        boolean isNumber = NumberUtil.isNumber(number);
        if (isNumber){
            Double obj = number == null ? 0 : Double.valueOf(number);
            return BigDecimal.valueOf(obj).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        }
        return 0.0;
    }
    public static String formatDigitString(Double num, int decimalPlace) {
        String format = "0";
        switch (decimalPlace) {
            case 0:
                format = String.format("%.0f", num);
                break;
            case 1:
                format = String.format("%.1f", num);
                break;
            case 2:
                format = String.format("%.2f", num);
                break;
            case 3:
                format = String.format("%.3f", num);
                break;
            case 4:
                format = String.format("%.4f", num);
                break;
            default:
                break;
        }
        return format;
    }

    /**
     * String  保留位小数
     *
     * @param num
     * @param decimalPlace
     * @return
     */
    public static String formatDigitStrings(String num, int decimalPlace) {
        DecimalFormat fm = null;
        switch (decimalPlace) {
            case 0:
                fm = new DecimalFormat("0");
                break;
            case 1:
                fm = new DecimalFormat("0.0");
                break;
            case 2:
                fm = new DecimalFormat("0.00");
                break;
            case 3:
                fm = new DecimalFormat("0.000");
                break;
            case 4:
                fm = new DecimalFormat("0.0000");
                break;
            default:
                break;
        }

        if (fm == null) {
            return num;
        }

        return fm.format(new BigDecimal(num));
    }

    /**
     * 将数据转换为保留指定小数位数（0，1，2）格式的数。向下取值：如 5.567 -> 5.56 -5.567 -> -5.56
     */
    public static double formatDigit_down(double num, int decimalPlace) {
        DecimalFormat fm = null;
        switch (decimalPlace) {
            case 0:
                fm = new DecimalFormat("#######.##");
                break;
            case 1:
                fm = new DecimalFormat("#######.##");
                break;
            case 2:
                fm = new DecimalFormat("#######.##");
                break;
            case 3:
                fm = new DecimalFormat("#######.##");
                break;
            case 4:
                fm = new DecimalFormat("#######.##");
                break;
            default:
                break;
        }

        if (fm == null) {
            return num;
        }

        return 0.00;
    }




    /**
     * 获取当前是否是闰年
     *
     * @return
     */
    public static int Years() {
        int num = 0;
        LocalDate date = LocalDate.now();

        boolean leapYear = date.isLeapYear();
        if (leapYear) {
            num = 13;
        }
        return num;
    }

    public static int intValue(Double v) {
        return new Double(v).intValue();
    }



}
