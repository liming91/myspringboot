package com.ming.exception;

import com.ming.bean.MessageEnum;

/**
 * 自定义异常
 */
public class ServiceException extends RuntimeException  {
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

    public ServiceException(MessageEnum errorCode) {
        super(String.valueOf(errorCode.getIndex()));
        this.errorCode = errorCode.getIndex();
        this.errorMsg = errorCode.getValue();
    }

    public ServiceException(MessageEnum errorCode, Throwable throwable) {
        //super(String.valueOf(errorCode.getIndex()), throwable);
        this.errorCode = errorCode.getIndex();
        this.errorMsg = errorCode.getValue();
    }
}
