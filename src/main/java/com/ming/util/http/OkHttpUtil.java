package com.ming.util.http;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @Author liming
 * @Date 2024/7/19 16:30
 */
@Slf4j
public class OkHttpUtil {


    public static final MediaType jsonType = MediaType.parse("application/json; charset=utf-8");

    //请求配置
    public static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(8000, TimeUnit.MILLISECONDS)
            .readTimeout(8000, TimeUnit.MILLISECONDS)
            .build();


    /**
     * 整理请求头信息
     *
     * @param headersParams
     * @return
     */
    public static Headers SetHeaders(Map<String, String> headersParams) {
        Headers headers = null;
        Headers.Builder headersbuilder = new Headers.Builder();
        if (ObjectUtil.isNotEmpty(headersParams)) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next();
                headersbuilder.add(key, headersParams.get(key));
            }
        }
        headers = headersbuilder.build();
        return headers;
    }


    /**
     * 发送post请求通过JSON参数
     *
     * @param reqUrl  请求url
     * @param param   请求参数
     * @param headMap 请求头
     */
    public static JsonObject sendPostByJsonAndHeader(String reqUrl, Object param, Map<String, String> headMap) {
        JsonObject jsonObject = null;
        try {
            String paramStr = JSON.toJSONString(param);

            RequestBody requestBody = RequestBody.create(jsonType, paramStr);
            long startTime = System.currentTimeMillis();

            Headers headers = SetHeaders(headMap);

            Request.Builder builder = new Request.Builder().url(reqUrl).post(requestBody).headers(headers);
            String body = null;
            try (Response response = okHttpClient.newCall(builder.build()).execute()) {
                body = response.body().string();
                jsonObject = new Gson().fromJson(body, JsonObject.class);
            } catch (Exception e) {
                log.error("[OkHttpUtil]-[sendPostByJsonAndHeader]-[response:{},Exception:{}]", body, ExceptionUtils.getMessage(e));
                //throw new ServiceException(ManagerResultStatusEnum.API_REQUEST_EXCEPTION.getStatus(), ManagerResultStatusEnum.API_REQUEST_EXCEPTION.getComment());
            } finally {
                long endTime = System.currentTimeMillis();
                log.info("OkHttpUtil:{} cost time:{}", reqUrl.substring(reqUrl.lastIndexOf("/") + 1),
                        (endTime - startTime));
            }
        } catch (Exception e) {
            log.error("[sendPostByJsonAndHeader:OkHttpUtil2]-[error:{}]", e);
        }
        return jsonObject;
    }

    /**
     * 发送get请求多个请求头
     *
     * @param url     http请求地址
     * @param headMap http请求头
     * @return
     */
    public JsonObject sendGetByHeader(String url, Map<String, String> headMap) {
        // 设置HTTP请求参数
        JsonObject jsonObject = null;
        try {
            Headers setHeaders = SetHeaders(headMap);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).headers(setHeaders).build();

            String body = null;
            try (Response response = okHttpClient.newCall(request).execute()) {
                body = response.body().string();
                jsonObject = new Gson().fromJson(body, JsonObject.class);
            } catch (Exception e) {
                log.error("[OkHttpUtil]-[sendGetByHeader]-[response:{},Exception:{}]", body, ExceptionUtils.getMessage(e));
                //throw new ServiceException(ManagerResultStatusEnum.API_REQUEST_EXCEPTION.getStatus(), ManagerResultStatusEnum.API_REQUEST_EXCEPTION.getComment());
            }
        } catch (Exception e) {
            log.error("[sendGetByHeader:OkHttpUtil2]-[error:{}]", e);
        }

        return jsonObject;
    }

    /**
     * 发送get请求
     *
     * @param url
     * @return
     */
    public JsonObject sendGet(String url) {
        // 设置HTTP请求参数
        JsonObject jsonObject = null;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            String body = null;
            try (Response response = okHttpClient.newCall(request).execute()) {
                body = response.body().string();
                jsonObject = new Gson().fromJson(body, JsonObject.class);
            } catch (Exception e) {
                log.error("[OkHttpUtil]-[sendGet]-[response:{},Exception:{}]", body, ExceptionUtils.getMessage(e));
                //throw new ServiceException(ManagerResultStatusEnum.API_REQUEST_EXCEPTION.getStatus(), ManagerResultStatusEnum.API_REQUEST_EXCEPTION.getComment());
            }
        } catch (Exception e) {
            log.error("[sendGet:OkHttpUtil2]-[error:{}]", e);
        }

        return jsonObject;
    }


    /**
     * 发送post请求通过JSON参数
     *
     * @param reqUrl 请求url
     * @param param  请求参数
     */
    public JsonObject sendPostByJson(String reqUrl, Object param) {
        JsonObject jsonObject = null;
        try {
            String paramStr = JSON.toJSONString(param);

            RequestBody requestBody = RequestBody.create(jsonType, paramStr);
            long startTime = System.currentTimeMillis();

            Request.Builder builder = new Request.Builder().url(reqUrl).post(requestBody);
            String body = null;
            try (Response response = okHttpClient.newCall(builder.build()).execute()) {
                body = response.body().string();
                jsonObject = new Gson().fromJson(body, JsonObject.class);
            } catch (Exception e) {
                log.error("[OkHttpUtil]-[sendPostByJsonAndHeader]-[response:{},Exception:{}]", body, ExceptionUtils.getMessage(e));
                //throw new ServiceException(ManagerResultStatusEnum.API_REQUEST_EXCEPTION.getStatus(), ManagerResultStatusEnum.API_REQUEST_EXCEPTION.getComment());
            } finally {
                long endTime = System.currentTimeMillis();
                log.info("OkHttpUtil:{} cost time:{}", reqUrl.substring(reqUrl.lastIndexOf("/") + 1),
                        (endTime - startTime));
            }
        } catch (Exception e) {
            log.error("[sendPostByJsonAndHeader:OkHttpUtil2]-[error:{}]", e);
        }
        return jsonObject;
    }

    /**
     * get请求带头部参数
     *
     * @param url
     * @param params
     * @param headMap
     * @return
     */
    public static String httpGetAndHead(String url, Map<String, Object> params, Map<String, String> headMap) {
        // 设置HTTP请求参数
        String result = null;
        url += getParams(params);
        Headers setHeaders = SetHeaders(headMap);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).headers(setHeaders).build();
        System.out.println(url);
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String string = response.body().string();
            result = String.valueOf(JSON.parse(string));
        } catch (Exception e) {
            log.error("调用三方接口出错", e);
        }
        return result;
    }

    public static String getParams(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder("?");
        if (ObjectUtil.isNotEmpty(params)) {
            for (Map.Entry<String, Object> item : params.entrySet()) {
                Object value = item.getValue();
                if (ObjectUtil.isNotEmpty(value)) {
                    sb.append("&");
                    sb.append(item.getKey());
                    sb.append("=");
                    sb.append(value);
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }


}

