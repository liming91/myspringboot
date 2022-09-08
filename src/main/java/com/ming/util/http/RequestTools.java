package com.ming.util.http;

import com.alibaba.fastjson.JSONObject;
import com.ming.component.HttpClientPool;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RequestTools {

    /**
     * 处理get post请求
     *
     * @param url
     * @param requestMethod
     * @param paramsMap
     * @return
     */
    public static String processHttpRequest(String url, String requestMethod, Map<String, String> paramsMap) {

        List<BasicNameValuePair> formParams = new ArrayList<>();
        for (Iterator<String> it = paramsMap.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            String value = paramsMap.get(key);
            formParams.add(new BasicNameValuePair(key, value));
        }
        if ("post".equals(requestMethod)) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
            return doRequest(httpPost, null, formParams);
        } else if ("get".equals(requestMethod)) {
            HttpGet httpGet = new HttpGet(url);
            return doRequest(null, httpGet, formParams);
        }
        return "";
    }

    private static String doRequest(HttpPost httpPost, HttpGet httpGet, List<BasicNameValuePair> formParams) {
        try {
            CloseableHttpResponse response = null;
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formParams);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(25000).setConnectTimeout(3000).build();
            if (null != httpPost) {
                urlEncodedFormEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8"));
                httpPost.setEntity(urlEncodedFormEntity);
                httpPost.setConfig(requestConfig);
                response = HttpClientPool.getHttpClient().execute(httpPost);
            } else {
                httpGet.setConfig(requestConfig);
                response = HttpClientPool.getHttpClient().execute(httpGet);
            }
            HttpEntity entity = response.getEntity();
            String str = EntityUtils.toString(entity, "UTF-8");
            if (null == str || "".equals(str)) {
                return "";
            } else {
                return str;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * json 格式的post方法
     *
     * @param postUrl
     * @param jsonObject
     * @return
     */
    public static String processPostJson(String postUrl, JSONObject jsonObject) throws IOException {
        HttpClient defaultHttpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(postUrl);
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        String s = null;
        StringEntity entity = new StringEntity(jsonObject.toString(), "utf-8");
        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8"));
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(25000).setConnectTimeout(3000).build();
        httpPost.setEntity(entity);
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = HttpClientPool.getHttpClient().execute(httpPost);
        HttpEntity httpEntity = response.getEntity();
        if (httpEntity != null) {
            InputStream inputStream = httpEntity.getContent();
            s = convertStreamToString(inputStream);
            httpPost.abort();
        }
        return s;
    }

    private static String convertStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
