package com.ming.aspect;

import com.ming.annotation.TestAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @Author liming
 * @Date 2023/3/30 14:27
 */
@Aspect
@Component
@Slf4j
public class DemoAspect {

    private static HttpServletRequest request;

    /**
     * 前置通知（Before advice）- 在目标方便调用前执行通知
     * 后置通知（After advice）- 在目标方法完成后执行通知
     * 返回通知（After returning advice）- 在目标方法执行成功后，调用通知
     * 异常通知（After throwing advice）- 在目标方法抛出异常后，执行通知
     * 环绕通知（Around advice）- 在目标方法调用前后均可执行自定义逻辑
     * <p>
     * 这里定义了一个总的匹配规则，以后拦截的时候直接拦截point()方法即可，无须去重复写execution表达式
     */
    @Pointcut("@annotation(com.ming.annotation.TestAnnotation)")
    public void point() {
    }

    @Before(value = "point()&&@annotation(testAnnotation)")
    public void doBefore(JoinPoint joinPoint, TestAnnotation testAnnotation) {
        log.info("===============================Start========================");
        //log.info("IP                 : {}", request.getRemoteAddr());
        log.info("目标方法名为: {}", joinPoint.getSignature().getName());
        log.info("目标方法所属类的简单类名: {}", joinPoint.getSignature().getDeclaringType().getSimpleName());
        log.info("目标方法所属类的类名: {}", joinPoint.getSignature().getDeclaringTypeName());
        log.info("目标方法声明类型: {}", Modifier.toString(joinPoint.getSignature().getModifiers()));
        log.info("===============================参数========================");
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            System.out.println("第" + (i + 1) + "个参数为:" + args[i]);
        }
        System.out.println("参数长度==：" + args.length);
        System.out.println("被代理的对象:" + joinPoint.getTarget());
        System.out.println("代理对象自己:" + joinPoint.getThis());
        log.info("拦截的注解的参数：模块{},描述{}", testAnnotation.module(), testAnnotation.desc());
    }


    @Around("point()&&@annotation(testAnnotation)")
    public Object doAround(ProceedingJoinPoint pjp, TestAnnotation testAnnotation) throws Throwable {
        System.out.println("环绕通知：");

        try {
            log.info("环绕通知的前置通知执行了...");
            //获取方法的签名
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            TestAnnotation annotation = method.getAnnotation(TestAnnotation.class);
            if(annotation != null){
                System.out.println(annotation.desc());
            }
            //获取参数
            Object[] args = pjp.getArgs();
            Object paramData = args[0];
            Class<?> paramDataClass = paramData.getClass();
            Object result = pjp.proceed();
            String phone =  String.valueOf(paramDataClass.getMethod("getPhonenumber").invoke(paramData));
            log.info("环绕通知的后置通知执行了...");
            System.out.println(testAnnotation.module());
            System.out.println(testAnnotation.desc());
            return result;
        } catch (Throwable throwable) {
            log.info("环绕通知的异常通知执行了...");
            throw new RuntimeException(throwable);
        } finally {
            log.info("环绕通知的最终通知执行了...");
        }

    }

    @After("point()&&@annotation(testAnnotation)")
    public void doAfter(TestAnnotation testAnnotation) {
        log.info("===============================end========================");
    }
}
