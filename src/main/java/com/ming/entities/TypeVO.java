package com.ming.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TypeVO {

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 类型值
     */
    private int  TypeValue;
}
