package com.ming.strategy;

import com.ming.bean.Order;
import com.ming.bean.PayResult;

/**
 * 支付策略接所有支付方式的接口
 * @Author liming
 * @Date 2022/9/8 14:07
 */
public interface IStrategyService {
    /**
     * 返回订单支付结果
     *
     * @param order
     * @return
     */
    PayResult pay(Order order);
}
