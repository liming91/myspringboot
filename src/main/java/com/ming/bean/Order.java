package com.ming.bean;

import lombok.Data;

/**
 * 订单
 * @Author liming
 * @Date 2022/9/8 14:10
 */
@Data
public class Order {
    /**
     * 金额
     */
    private int amount;

    /**
     * 支付类型
     */
    private String paymentType;
}