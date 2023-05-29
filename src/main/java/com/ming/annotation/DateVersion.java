package com.ming.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止重复操作注解
 * @Author liming
 * @Date 2023/5/29 17:19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateVersion {
    //表名
    String tableName() default "";

    //id字段名称
    String idName() default "id";

    //version字段名称
    String versionName() default "version";
}
