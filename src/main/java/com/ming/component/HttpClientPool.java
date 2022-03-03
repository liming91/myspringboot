package com.ming.component;

import lombok.val;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * http连接池
 */
public class HttpClientPool {

    private static PoolingHttpClientConnectionManager cm = null;

    //1.私有构造 防止new对象
    private HttpClientPool() {
    }

    //2.本类中创建实例
    static {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(400);
        cm.setDefaultMaxPerRoute(50);
    }

    //3.提供静态方法
    public static CloseableHttpClient getHttpClient() {
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(globalConfig).build();
        return httpClient;
    }
}
