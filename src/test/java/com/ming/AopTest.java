package com.ming;

import com.ming.aop.JdkService;
import com.ming.aop.Real;
import com.ming.aop.ServiceInvocationHandler;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Proxy;

/**
 *
 *
 * 	在 Spring 中 AOP 代理使用 JDK 动态代理和 CGLIB 代理来实现，默认如果目标对象是接口，则使用 JDK 动态代理，否则使用 CGLIB 来生成代理类。
 *
 * 	JDK代理原理:
 * 	JDK 动态代理是对接口进行的代理:代理类实现了接口,并继承了Proxy类:目标对象与代理对象没有什么直接关系,只是它们都实现了接口
 * 	并且代理对象执行方法时候,内部最终是委托目标对象执行具体的方法的。
 *
 * 	①JDK动态代理只提供接口的代理，不支持类的代理。他的核心是InvocationHandler接口和Proxy类，InvocationHandler 通过invoke()
 * 	方法调用目标类中的代码.接着，Proxy利用 InvocationHandler动态创建一个符合接口的的实例,生成目标类的代理对象。
 * 	invoke(Object proxy,Method method,Object[] args)：
 * 	proxy是最终生成的代理实例;
 * 	method 是被代理目标实例调用的方法;
 * 	args 是被代理目标实例某个方法的方法的参数列表, 在方法反射调用时使用。
 * 	2. Proxy类是真正创建代理实例的类，其中主要是使用 Proxy.newProxyInstance来创建。
 * @Author liming
 * @Date 2023/3/30 15:18
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AopTest {
    /**
     *
     *
     *  AOP 代理使用
     *  1、创建目标对象JdkService 包括他的实现类
     *
     *  2、创建InvocationHandler并传入目标对象
     *
     *  3、创建代理对象
     *
     *  4、使用代理对象执行方法
     * @param args
     */
    public static void main(String[] args) {
        // 目标对象
        JdkService realService = new Real();

        // 创建InvocationHandler并传入目标对象
        ServiceInvocationHandler handler = new ServiceInvocationHandler(realService);

        // 创建代理实例
        JdkService serviceProxy = (JdkService) Proxy.newProxyInstance(
                realService.getClass().getClassLoader(),
                realService.getClass().getInterfaces(),
                handler);

        // 调用代理实例的方法
        serviceProxy.run();
    }


}
