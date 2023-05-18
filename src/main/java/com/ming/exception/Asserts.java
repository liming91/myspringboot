package com.ming.exception;


import com.ming.enums.ResultCode;

/**
 * 断言处理类，用于抛出各种API异常
 * Created By Ranger on 2022/4/12.
 */
public class Asserts {
    public static void fail(String message) {
        throw new ServiceException(message);
    }

    public static void fail(ResultCode errorCode) {
        throw new ServiceException(errorCode);
    }
}
