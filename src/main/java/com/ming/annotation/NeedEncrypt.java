package com.ming.annotation;

import java.lang.annotation.*;


/**
 * 作用于需要对入参数进行加密处理的方法上
 * @Author liming
 * @Date 2023/11/3 9:55
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedEncrypt {
}
