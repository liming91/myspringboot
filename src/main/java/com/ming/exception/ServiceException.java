package com.ming.exception;

import com.ming.enums.ResultCode;

/**
 * 自定义异常
 */
public class ServiceException extends RuntimeException {
    /**
     * 错误码
     */
    protected long errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public ServiceException() {
        super("用户不存在");
    }

    public ServiceException(String message) {
        super(message);
        this.errorMsg = message;
    }

    public ServiceException(ResultCode errorCode) {
        super(String.valueOf(errorCode.getCode()));
        this.errorCode = errorCode.getCode();
        this.errorMsg = errorCode.getMessage();
    }

    public ServiceException(ResultCode errorCode, Throwable throwable) {
        super(String.valueOf(errorCode.getCode()), throwable);
        this.errorCode = errorCode.getCode();
        this.errorMsg = errorCode.getMessage();
    }
}
