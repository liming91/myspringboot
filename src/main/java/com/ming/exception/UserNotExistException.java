package com.ming.exception;

import com.ming.bean.MessageEnum;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpStatus;

/**
 * 自定义异常
 */
public class UserNotExistException extends RuntimeException  {
    /**
     * 错误码
     */
    protected long errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;
    public UserNotExistException() {
        super("用户不存在");
    }

    public UserNotExistException(String message) {
        super(message);
        this.errorMsg = message;
    }

    public UserNotExistException(MessageEnum errorCode) {
        super(String.valueOf(errorCode.getIndex()));
        this.errorCode = errorCode.getIndex();
        this.errorMsg = errorCode.getValue();
    }

    public UserNotExistException(MessageEnum errorCode,Throwable throwable) {
        super(String.valueOf(errorCode.getIndex()), throwable);
        this.errorCode = errorCode.getIndex();
        this.errorMsg = errorCode.getValue();
    }
}
