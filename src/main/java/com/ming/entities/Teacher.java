package com.ming.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测试浅拷贝深拷贝
 *
 * @Author liming
 * @Date 2023/8/31 13:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    private String name;

    private Address address;

}
