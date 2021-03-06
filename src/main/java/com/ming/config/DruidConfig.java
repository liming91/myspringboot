package com.ming.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源Druid
 * ConfigurationProperties注解绑定yml的initialSize等其他属性
 */
@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid(){
        return new DruidDataSource();
    }

    //Druid的监控
    //1、配置一个管理后台的servlet
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String,String> initParams = new HashMap<>();
        //设置初始化参数在ResourceServlet
        //    public static final String SESSION_USER_KEY    = "druid-user";
        //    public static final String PARAM_NAME_USERNAME = "loginUsername";
        //    public static final String PARAM_NAME_PASSWORD = "loginPassword";
        //    public static final String PARAM_NAME_ALLOW    = "allow";
        //    public static final String PARAM_NAME_DENY     = "deny";
        //    public static final String PARAM_REMOTE_ADDR   = "remoteAddress";
        initParams.put("loginUsername","admin");
        initParams.put("loginPassword","123456");
        initParams.put("allow","");//IP白名单,默认就是允许所有访问
        initParams.put("deny","192.168.1.4");//拦截本地访问

        bean.setInitParameters(initParams);
        return  bean;
    }

    //2.配置一个web监控的filter、WebStatFilter用于配置Web和Druid数据源之间的管理关联监控统计
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());

        Map<String,String> initParams = new HashMap<>();
//        初始化参数
//        public final static String PARAM_NAME_PROFILE_ENABLE         = "profileEnable";
//        public final static String PARAM_NAME_SESSION_STAT_ENABLE    = "sessionStatEnable";
//        public final static String PARAM_NAME_SESSION_STAT_MAX_COUNT = "sessionStatMaxCount";
//        public static final String PARAM_NAME_EXCLUSIONS             = "exclusions";
//        public static final String PARAM_NAME_PRINCIPAL_SESSION_NAME = "principalSessionName";
//        public static final String PARAM_NAME_PRINCIPAL_COOKIE_NAME  = "principalCookieName";
//        public static final String PARAM_NAME_REAL_IP_HEADER         = "realIpHeader";
        initParams.put("exclusions","*.js,*.css,/druid/*");//不拦截这些请求

        bean.setInitParameters(initParams);

        bean.setUrlPatterns(Arrays.asList("/*"));//拦截所有的请求
        return  bean;
    }

}
