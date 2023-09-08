package com.ming;

import com.alibaba.fastjson2.JSONArray;
import com.ming.bean.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liming
 * @Date 2023/9/8 15:18
 */
public class InstanceofTest {
    @Test
    public void test1() {
        //obj instance class obj是引用对象 class是类或者接口
        //obj是class的实例对象
        //obj是class的直接子类或者间接子类
        //obj是class接口的实现类
        ArrayList arrayList = new ArrayList();

        User user = new User();
        if (user instanceof User) {
            System.out.println(true);
        }
        JSONArray jsonArray = new JSONArray();
        if (jsonArray instanceof ArrayList) {
            System.out.println(true);
        }
        Double d = 0.3;
        if (d instanceof Double) {
            System.out.println(true);
        }
        if (arrayList instanceof List) {
            System.out.println(true);
        }
    }
}
