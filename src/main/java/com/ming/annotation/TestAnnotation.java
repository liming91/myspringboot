package com.ming.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author liming
 * @Date 2022/9/2 14:03
 */
@Documented
@Target({TYPE,METHOD})
@Retention(RUNTIME)
public @interface TestAnnotation {
    public String module() default "";

    public String desc() default "描述";
}
