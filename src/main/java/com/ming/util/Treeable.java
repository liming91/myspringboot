package com.ming.util;

import java.util.List;

/**
 * @ClassName Treeable
 * @Author 程序员Mars
 * @Date 2024/3/22 17:34
 * @Version 1.0
 */
public interface Treeable<T> {
    Object getId();

    Object getPid();

    Long getDeep();

    Long getSort();

    List<T> getChildren();

    void setChildren(List<T> children);
}
