package com.ming.aop;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author liming
 * @Date 2025/2/17 10:51
 */
public class MyMethodInterceptor implements MethodInterceptor {
    // intercept方法会在目标方法执行前后被调用
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("Before method: " + method.getName());
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("After method: " + method.getName());
        return result;
    }

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CglibTarget.class); // 设置要代理的目标类
        enhancer.setCallback(new MyMethodInterceptor()); // 设置回调

        CglibTarget proxy = (CglibTarget) enhancer.create(); // 创建代理实例
        proxy.sayHello(); // 通过代理调用方法
    }
}
