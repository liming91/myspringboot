package com.ming.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;

public class PercentUtil {

    /**
     * 计算两个int数字百分比
     * @param x 分子（减完之后的差值）
     * @param y 分母
     * @return
     */
    public static String getPercent (int x, int y) {
        if (x == 0 && y == 0) {
            return "0";
        }
        if (x == 0 && y != 0) {
            return "0";
        }
        if (x != 0 && y == 0) {
            return "0";
        }
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        //numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(0);
        //100% = 100/100 = 1
        String res = numberFormat.format(((float)x / (float)y) * 100);
        return res;
    }

    //计算环比  环比增长率=（本期数－上期数）/上期数×100%
     public static  int  AC(int localEgyValues ,int lastEgyValues ){

         Format format = new DecimalFormat("0");
         //上年同比率
         Integer equGrowth = 0;
         if (localEgyValues == 0 && lastEgyValues == 0 ){
             equGrowth = 0;
         }else if (localEgyValues == 0 && lastEgyValues != 0 ){
             equGrowth = 0;
         }else  if(localEgyValues != 0 && lastEgyValues == 0){
             equGrowth = 0;
         }else { equGrowth = Integer.valueOf(format.format((double) ((localEgyValues-lastEgyValues) / lastEgyValues)));  }
        return  equGrowth;
     }

     /**
     * @Description:
     * @param: localEgyValues	 * @param: preEgyValues
     * @return: int
     * @Author: hpz
     * @Date: 2020/7/18 9:30
     **/

     // 计算同比  同比增长率=（本期数－同期数）/同期数×100%。
     public  static  int SC(int localEgyValues,int preEgyValues){
         Format format = new DecimalFormat("0");
         //全年环比率
         Integer ringGrowth = 0 ;
         if (localEgyValues == 0 && preEgyValues == 0 ){
             ringGrowth = 0;
         }else if (localEgyValues == 0 && preEgyValues != 0 ){
             ringGrowth = 0;
         }else  if(localEgyValues != 0 && preEgyValues == 0){
             ringGrowth = 0;
         }else {
             ringGrowth =  Integer.valueOf(format.format((double)((localEgyValues-preEgyValues) / preEgyValues))) ;
         }
         return  ringGrowth;
     }

    /**
     * // 计算同比  同比增长率=（本期数－同期数）/同期数×100%。  不同年
     * @Author: hpz
     * @Date: 2020/10/16 15:30
     **/
    public  static  Double SC(Double localEgyValues,Double preEgyValues){
        //全年环比率
        Double ringGrowth = 0.0 ;
        // 本期
        double amount = localEgyValues == null ? 0.0 : localEgyValues;
        // 同期
        double samePeriod = preEgyValues == null ? 0.0 : preEgyValues;
        BigDecimal localEgyValuesB = BigDecimal.valueOf(amount);
        BigDecimal preEgyValuesB = BigDecimal.valueOf(samePeriod);
        if (ringGrowth.equals(amount) && ringGrowth.equals(samePeriod)){
        }else if (ringGrowth.equals(amount) && !ringGrowth.equals(samePeriod) ){
        }else  if(!ringGrowth.equals(amount)  && ringGrowth.equals(samePeriod)){
        }else {
            BigDecimal divide = (localEgyValuesB).subtract(preEgyValuesB).divide(preEgyValuesB,2,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal divide100 = divide.multiply(BigDecimal.valueOf(100.00));
            ringGrowth= divide.setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        }



        return  ringGrowth;
    }

    //计算环比  环比增长率=（本期数－上期数）/上期数×100%  同年相比
    public static  Double  AC(Double localEgyValues,Double lastEgyValues ){
        // 本期
        double amount = localEgyValues == null ? 0.0 : localEgyValues;
        // 上期
        double lastPeriod = lastEgyValues == null ? 0.0 : lastEgyValues;

        //上年同比率
        Double equGrowth = 0.0;
        BigDecimal localEgyValuesB =  BigDecimal.valueOf(amount);
        BigDecimal lastEgyValuesB  =  BigDecimal.valueOf(lastPeriod);
        if (equGrowth.equals(amount) && equGrowth.equals(lastPeriod) ){
        }else if (equGrowth.equals(amount) && !equGrowth.equals(lastPeriod) ){
        }else  if(!equGrowth.equals(amount) && equGrowth.equals(lastPeriod) ){
        }else {
            BigDecimal divide =   (localEgyValuesB).subtract(lastEgyValuesB).divide(lastEgyValuesB,2,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal divide100 = divide.multiply(BigDecimal.valueOf(100.00));
            equGrowth= divide.setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        }
        if (!equGrowth.equals(amount) && !equGrowth.equals(lastPeriod) ){
            BigDecimal divide =    (localEgyValuesB).subtract(lastEgyValuesB).divide(lastEgyValuesB,2,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal divide100 = divide.multiply(BigDecimal.valueOf(100.00));
            equGrowth= divide.setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        }
        return  equGrowth;
    }

    /**
     * 两个double 的差值
     * @Author: hpz
     * @Date: 2020/10/19 13:28
     **/
    public static  Double  twoSubtract(double num1,double num2){
        BigDecimal subtract = BigDecimal.valueOf(num1).subtract(BigDecimal.valueOf(num2));
        double value = subtract.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        return value;
    }
}
