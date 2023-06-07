package com.ming.aspect;

import com.ming.annotation.ForbidRepeatSubmit;
import com.ming.enums.SubmitEnum;
import com.ming.util.http.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: the_pure
 * @CreateDate: 2021/11/18 14:09
 */
@Aspect
@Component
public class ForbidRepeatSubmitHandler {
    @Autowired
    private RedisTemplate redisTemplate;
 
    /**
     * 切点
     */
    @Pointcut("@annotation(com.ming.annotation.ForbidRepeatSubmit)")
    public void pointcut() {}
 
    /**
     * 环绕通知
     *
     * @return
     */
    @Around("pointcut()&&@annotation(ForbidRepeatSubmit)")
    public Result<?> around(ProceedingJoinPoint joinPoint, ForbidRepeatSubmit ForbidRepeatSubmit) {
        Object obj = null;
        // 操作方法前判定是否提交过
        // 获取request请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        SubmitEnum type = ForbidRepeatSubmit.type();
        String key = "";
        if(SubmitEnum.IP.equals(type)){// IP
            // ForbidRepeatSubmit+地址+访问路径+访问方法类型
            key = "ForbidRepeatSubmit:"+request.getRemoteAddr().hashCode()
                    +":"+request.getServletPath()
                    +":"+request.getMethod();
        } else {// TOKEN
            // ForbidRepeatSubmit+TOKEN+访问路径+访问方法类型
            key = "ForbidRepeatSubmit:"+request.getHeader("Authorization").hashCode()
                    +":"+request.getServletPath()
                    +":"+request.getMethod();
        }
        System.err.println("redisKey:"+key);
 
        // 进行操作
        if(redisTemplate.opsForValue().get(key) == null) {
            try {
                obj = joinPoint.proceed();
                System.err.println("操作成功！");
                // 将该操作记录到redis
                redisTemplate.opsForValue().set(key,0,ForbidRepeatSubmit.time(), TimeUnit.SECONDS);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {// 重复提交不操作
            return Result.success(false, 1111, "请勿重复提交");
        }

         return Result.success();

    }
}