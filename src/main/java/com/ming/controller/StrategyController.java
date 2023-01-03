package com.ming.controller;

import com.ming.bean.Order;
import com.ming.bean.PayResult;
import com.ming.strategy.PayContext;
import com.ming.strategy.IStrategyService;
import com.ming.strategy.AliPayImpl;
import com.ming.strategy.WeChatPayImpl;
import com.ming.util.http.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author liming
 * @Date 2022/9/8 14:29
 */
@Api(tags = "支付方式测试")
@RestController
@RequestMapping("/strategy")
public class StrategyController {
    @Autowired
    @Qualifier(value = "AliPay")
    private  IStrategyService iStrategyService;

    @Autowired
    private ApplicationContext applicationContext;

    @ApiOperation("支付结果")
    @GetMapping("/getPayResult")
    public ResponseResult<?> getPayResult(@RequestParam("amount") double amount,
                                          @RequestParam("paymentType") String paymentType) {
        Order order = new Order();
        order.setAmount(amount);
        order.setPaymentType(paymentType);
        // 根据支付类型获取对应的策略 bean
        IStrategyService payment = applicationContext.getBean(order.getPaymentType(), IStrategyService.class);

        // 开始支付
        //PayResult payResult = payment.pay(order);
        PayContext payContext = new PayContext(payment);
        PayResult payResult = payContext.executeStrategy(order);
        return ResponseResult.success(payResult);
    }


    @ApiOperation("支付结果")
    @GetMapping("/getPayResult2")
    public ResponseResult<?> getPayResult2(@RequestParam("amount") double amount,
                                          @RequestParam("paymentType") String paymentType) {
        Order order = new Order();
        order.setAmount(amount);
        order.setPaymentType(paymentType);
        IStrategyService iStrategyService1 = new WeChatPayImpl();
        PayResult pay1 = iStrategyService1.pay(order);
        IStrategyService iStrategyService2 = new AliPayImpl();
        PayResult pay2 = iStrategyService2.pay(order);

        return ResponseResult.success(pay1);
    }

}