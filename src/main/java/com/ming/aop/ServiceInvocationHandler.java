package com.ming.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 接下来创建一个实现了InvocationHandler接口的类，用来处理对代理实例方法的调用。
 * @Description
 * @Author liming
 * @Date 2025/1/2 9:24
 */
public class ServiceInvocationHandler implements InvocationHandler {

    private final Object target;

    public ServiceInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 在目标方法执行前加入日志或其它操作
        System.out.println("Before method " + method.getName());

        // 执行目标对象的方法
        Object result = method.invoke(target, args);

        // 在目标方法执行后加入日志或其它操作
        System.out.println("After method " + method.getName());

        return result;
    }


}
