package com.ming.strategy;

import com.ming.bean.Order;
import com.ming.bean.PayResult;
import com.ming.strategy.IStrategyService;
import org.springframework.stereotype.Service;

/**
 * 微信支付
 *
 * @Author liming
 * @Date 2022/9/8 14:27
 */
@Service("WeChatPay")
public class WeChatPayImpl implements IStrategyService {
    @Override
    public PayResult pay(Order order) {
        return new PayResult("微信支付成功!");
    }
}