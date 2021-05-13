package com.ming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 使用WebMvcConfigurerAdapter可以来扩展springmvc的功能
 */
@Configuration
//@EnableWebMvc 不要接管springmvc
public class MyMvcConfig extends WebMvcConfigurerAdapter {
    //视图映射
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //super.addViewControllers(registry);
        //浏览器发送http://localhost:8082/ming请求来到success页面
        registry.addViewController("/ming").setViewName("success");
    }

    //所有的WebMvcConfigurerAdapter组件都会一起起作用
    @Bean //将组件注册在容器
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
              registry.addViewController("/").setViewName("login");
              registry.addViewController("/index.html").setViewName("login");
            }
        };
        return adapter;
    }
}
