package com.ming.exception;

import cn.hutool.http.HttpUtil;
import com.ming.enums.ResultCode;
import com.ming.util.http.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                      HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return Result.failure(ResultCode.REQUEST_FAIL);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public Result handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        long errorCode = e.errorCode;
        return Result.failure((int) errorCode, e.errorMsg);
    }


    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return Result.failure(500, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handle(MethodArgumentNotValidException exception) {
        log.error("参数异常信息：", exception);
        StringBuffer message = new StringBuffer();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            message.append(errorMessage).append(",");
        });
        String result = message.toString();
        return Result.failure(400, result.substring(0, result.length() - 1));
    }

}
