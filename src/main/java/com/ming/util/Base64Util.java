package com.ming.util;


import org.apache.commons.codec.binary.Base64;

/**
 * @Author liming
 * @Date 2023/8/18 16:32
 */
public class Base64Util {

    /**
     * 编码
     *
     * @param bytes
     * @return
     */
    public static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }


    /**
     * 解码
     *
     * @param bytes
     * @return
     */
    public static String decode(byte[] bytes) {
        return new String(Base64.decodeBase64(bytes));
    }

    public static void main(String[] args) {
        String string = "西安";
        //编码
        String encode = encode(string.getBytes());
        System.out.println(string + "\t编码后的字符串为：" + encode);
        //解码
        String decode = decode(encode.getBytes());
        System.out.println(encode + "\t字符串解码后为：" + decode);
    }

}
