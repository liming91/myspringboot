package com.ming.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//
//        if (o instanceof Teacher){
//            Teacher teacher = (Teacher) o;
//            //比较两个对象的每个属性是否相同
//            return Objects.equals(this.name, teacher.name) && Objects.equals(this.address, teacher.address);
//        }
//        return false;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(name, teacher.name) && Objects.equals(address, teacher.address);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }



    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }
}
