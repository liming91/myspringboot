package com.ming.util;

/**
 * @Author liming
 * @Date 2023/2/1 11:35
 */
public class StrUtil {

    public static String subStr(String str) {
        String s = "002_A";
        System.out.println(s.substring(0, s.indexOf("_")));
        System.out.println(s.substring(s.lastIndexOf("_") + 1, s.length()));
        return s;
    }
}
