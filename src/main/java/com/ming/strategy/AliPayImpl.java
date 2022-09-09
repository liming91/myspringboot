package com.ming.strategy;

import com.ming.bean.Order;
import com.ming.bean.PayResult;
import com.ming.strategy.IStrategyService;
import org.springframework.stereotype.Service;

/**
 * @Author liming
 * @Date 2022/9/8 14:23
 */
@Service("AliPay")
public class AliPayImpl implements IStrategyService {
    @Override
    public PayResult pay(Order order) {
        return new PayResult("支付宝支付成功!");
    }
}