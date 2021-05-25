package com.ming.exception;

import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpStatus;

/**
 * 自定义异常
 */
public class UserNotExistException extends RuntimeException  {

    public UserNotExistException() {
        super("用户不存在");
    }


//    public class RRException extends RuntimeException {
//        private static final long serialVersionUID = 1L;
//
//        protected String msg;
//        protected int code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
//        protected String extraData;
//
//        private RRException(String msg) {
//            super(msg);
//
//            this.msg = msg;
//        }
//
//        public static RRException create(String msg, Object... args) {
//            return new RRException(MessageFormatter.arrayFormat(msg, args).getMessage());
//        }
//
//        public RRException from(Throwable e) {
//            this.initCause(e);
//
//            return this;
//        }
//    }
}
