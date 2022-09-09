package com.ming.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 返回结果类
 *
 * @Author liming
 * @Date 2022/9/8 14:08
 */
@Data
@AllArgsConstructor
public class PayResult {

    /**
     * 支付结果
     */
    private String result;
}