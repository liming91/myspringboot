package com.ming.exception;


import com.ming.bean.MessageEnum;

/**
 * 断言处理类，用于抛出各种API异常
 * Created By Ranger on 2022/4/12.
 */
public class Asserts {
    public static void fail(String message) {
        throw new UserNotExistException(message);
    }

    public static void fail(MessageEnum errorCode) {
        throw new UserNotExistException(errorCode);
    }
}
