package com.ming.util;

import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;

/**
 * @Author liming
 * @Date 2022/12/2 11:30
 */
public class HutoolUtil {
    public static void main(String[] args) {
        BigDecimal round = NumberUtil.round(5.8774717541114375E-39, 2);
        System.out.println(round);
    }
}
