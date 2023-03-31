package com.ming.annotation;

import com.ming.enums.SensitiveTypeEnum;

import java.lang.annotation.*;

/**
 * @Author liming
 * @Date 2023/3/31 10:50
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Desensitized {
    //脱敏类型(规则)
    SensitiveTypeEnum type();
}
