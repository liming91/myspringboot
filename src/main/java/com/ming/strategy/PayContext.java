package com.ming.strategy;

import com.ming.bean.Order;
import com.ming.bean.PayResult;

/**
 * 业务上下文类
 *
 * @Author liming
 * @Date 2022/9/8 16:35
 */
public class PayContext {
    //持有一个具体策略的对象
    private IStrategyService iStrategyService;

    /**
     * 构造函数，传入一个具体策略对象
     *
     * @param iStrategyService 具体策略对象
     */
    public PayContext(IStrategyService iStrategyService) {
        this.iStrategyService = iStrategyService;
    }

    public void setIStrategyService(IStrategyService iStrategyService) {
        this.iStrategyService = iStrategyService;
    }

    /**
     * 策略方法
     *
     * @param order
     * @return
     */
    public PayResult executeStrategy(Order order) {
        return iStrategyService.pay(order);
    }

}