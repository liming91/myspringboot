package com.ming.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作用于对返回值进行解密处理的方法上
 * @Author liming
 * @Date 2023/11/3 9:54
 */
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedDecrypt {
}
