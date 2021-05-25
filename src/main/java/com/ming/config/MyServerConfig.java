package com.ming.config;

import com.ming.filter.MyFilter;
import com.ming.listener.MyListener;
import com.ming.servlet.MyServlet;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


/**
 * 服务器有关的配置
 */
@Configuration
public class MyServerConfig{


    //注册servlet三大组件
    @Bean
    public ServletRegistrationBean myServlet(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new MyServlet(),"/myServlet");
        registrationBean.setLoadOnStartup(1);//启动顺序
        return  registrationBean;
    }

    //注册Filter
    @Bean
    public FilterRegistrationBean myFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new MyFilter());
        registrationBean.setUrlPatterns(Arrays.asList("/hello","/myServlet"));
        return  registrationBean;
    }

    //注册Listener
    @Bean
    public ServletListenerRegistrationBean myListener(){
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean<MyListener>(new MyListener());
        return  servletListenerRegistrationBean;
    }

    //配置嵌入式的servlet容器
    @Bean
    public EmbeddedServletContainerCustomizer myEmbeddedServletContainerCustomizer(){
        return new EmbeddedServletContainerCustomizer() {
            //定制嵌入式的servlet容器相关的规则
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                //container.setPort(8091);
            }
        };
    }
}
