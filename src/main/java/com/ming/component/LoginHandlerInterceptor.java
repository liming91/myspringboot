package com.ming.component;

import com.ming.annotation.ResultAnnotation;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.springframework.web.method.HandlerMethod;



/**
 * 登录拦截器
 */
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {
    public static final String RESPONSE_RESULT_ANN = "RESPONSE-RESULT-ANN";
    //目标方法执行之前
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Object user = request.getSession().getAttribute("loginUser");
//        if(user == null){
//            //未登录,返回登录页面
//            request.setAttribute("msg","没有权限先登录");
//            request.getRequestDispatcher("/index.html").forward(request,response);
//            return false;
//        }else{
//            //已登录，放行请求
//            return true;
//        }
//
//    }

    /**
     * 拦截请求，是否此请求返回的值需要包装，运行时解析 @Result 注解
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Class<?> beanType = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();
            if (beanType.isAnnotationPresent(ResultAnnotation.class)) {
                request.setAttribute(RESPONSE_RESULT_ANN, beanType.getAnnotation(ResultAnnotation.class));
            } else if (method.isAnnotationPresent(ResultAnnotation.class)) {
                request.setAttribute(RESPONSE_RESULT_ANN, method.getAnnotation(ResultAnnotation.class));
            }
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
