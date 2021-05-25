package com.ming.controller;

import com.ming.exception.UserNotExistException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常处理器
 */
@ControllerAdvice
public class MyExceptionHandler {

    /**
     * 要处理的异常
     *
     * bug:浏览器客户端访问返回的都是json数据，应该浏览器访问返回页面，客户端访问返回json
     * @return
     */
//    @ResponseBody
//    @ExceptionHandler(UserNotExistException.class)
//    public Map<String,Object> handlerException(Exception e){
//        Map<String,Object> map = new HashMap<>();
//        map.put("code","user.notexist");
//        map.put("message",e.getMessage());
//
//        return  map;
//    }

    /**
     * 自适应返回json或html页面
     * @param e
     * @return
     */
    @ExceptionHandler(UserNotExistException.class)
    public String handlerException(Exception e, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        //传入我们自己的错误状态码 4xx 5xx,否则就不会进入定制的错误页面
        //Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        request.setAttribute("javax.servlet.error.status_code",500);
        map.put("code","user.notexist");
        map.put("message",e.getMessage());
        //将扩展的数据放在请求域中
        request.setAttribute("ext",map);
        //转发到error
        return  "forward:/error";
    }


}
