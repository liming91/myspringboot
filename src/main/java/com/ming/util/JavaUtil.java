package com.ming.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 */

public class JavaUtil {

	/**
	 * 日志类
	 */
	private static Logger logger = LoggerFactory.getLogger(JavaUtil.class);

	/**
	 * 判断map是否为空
	 * @param	map
	 * @return	boolean
	 */


	/**
	 * 返回restTemplate所需的HttpEntity对象
	 *
	 * @param data 需要排序并参与字符拼接的参数组
	 * @return HttpEntity对象
	 */
	public static HttpEntity<String> getFormEntity(String data){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> formEntity = new HttpEntity<>(data, headers);
		return formEntity;
	}


}
