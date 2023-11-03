package com.ming.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加密字段
 * @Author liming
 * @Date 2023/11/3 9:42
 */
@Target(value ={ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptField {

}
