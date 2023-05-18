package com.ming.annotation;

import com.ming.enums.SubmitEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: the_pure
 * @CreateDate: 2021/11/18 14:02
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ForbidRepeatSubmit {
    /**
     * 防止接口重复提交时间 单位秒 默认1s
     * @return
     */
    long time() default 1L;
 
    /**
     * 判定类型 默认使用IP方式来判定是否同一个人
     * @return
     */
    SubmitEnum type() default SubmitEnum.IP;
}