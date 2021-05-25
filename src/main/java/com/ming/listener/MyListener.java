package com.ming.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 自定义listener要用springboot提供的ServletListenerRegistrationBean
 * listener有很多主要监听servlet启动和销毁的Listener
 */
public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("contextInitialized初始化servlet...web应用启动");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println(".contextDestroyed销毁...关闭web应用以后,当前web项目销毁");

    }
}
