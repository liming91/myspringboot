package com.ming;

import cn.hutool.core.convert.Convert;
import com.ming.bean.Person;
import com.ming.service.IAsyncService;
import com.ming.util.QuarterUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MySpringBootApplicationTests {

//    //记录器
//    Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    Person person;
//
//    @Autowired
//    ApplicationContext ioc;
//
//    @Autowired
//    DataSource dataSource;
//
//    @Test
//    public void contextLoads(){
//        System.out.println(person);
//    }
//
//    @Test
//    public void helloService(){
//        System.out.println(ioc.containsBean("helloService"));
//    }
//
//    @Test
//    public void logTest(){
//        //日志的级别由低到高trace<debug<info<warn<error
//        //可以调整输出的日志的级别，日志只会在这个级别以后的高级别生效
//        logger.trace("这是trace日志，跟踪框架轨迹...");
//        logger.debug("这是debug调试信息日志...");
//        //springboot默认使用info级别的,root级别,可以调整logging.level.com.ming=trace
//        logger.info("这是info自己定义的输出日志..");
//        logger.warn("这是警告日志..");
//        logger.error("这是error日志...");
//    }
//
//    @Test
//    public void jdbcTest() throws SQLException {
//        //class org.apache.tomcat.jdbc.pool.DataSource
//        System.out.println("===========================dataSource:"+dataSource.getClass());
//        Connection connection = dataSource.getConnection();
//        System.out.println("connection==:"+connection);
//        connection.close();
//    }
//
//    @Autowired
//    HelloService helloService;
//
//    @Test
//    public void starterTest(){
//        String sayHello = helloService.sayHello("自定义starter");
//        System.out.println(sayHello);
//    }
    //记录器
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    Person person;

    @Autowired
    ApplicationContext ioc;

    @Autowired
    DataSource dataSource;
    @Autowired
    private IAsyncService iAsyncService;

//    @Autowired
//    private RestTemplate restTemplate;

    @Test
    public void contextLoads() {
        System.out.println(person);
    }

    @Test
    public void helloService() {
        System.out.println(ioc.containsBean("helloService"));
    }

    @Test
    public void logTest() {
        //日志的级别由低到高trace<debug<info<warn<error
        //可以调整输出的日志的级别，日志只会在这个级别以后的高级别生效
        logger.trace("这是trace日志，跟踪框架轨迹...");
        logger.debug("这是debug调试信息日志...");
        //springboot默认使用info级别的,root级别,可以调整logging.level.com.ming=trace
        logger.info("这是info自己定义的输出日志..");

        logger.warn("这是警告日志..");
        logger.error("这是error日志...");
    }

    @Test
    public void jdbcTest() throws SQLException {
        //class org.apache.tomcat.jdbc.pool.DataSource
        System.out.println("===========================dataSource:" + dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("connection==:" + connection);
        connection.close();
    }


    @Test
    public void timeTest() throws SQLException {
        String strDate = "2021-11-22";

        Date date = QuarterUtils.parseDate(strDate);


//        System.out.println(strDate + " 所在季度第一天日期？"
//                + QuarterUtils.formatDate(QuarterUtils.getFirstDateOfSeason(date)));
//        System.out.println(strDate + " 所在季度最后一天日期？"
//                + QuarterUtils.formatDate(QuarterUtils.getLastDateOfSeason(date)));
//        System.out.println(strDate + " 所在季度天数？" +QuarterUtils. getDayOfSeason(date));
//        System.out.println(strDate + " 所在季度已过多少天？" + QuarterUtils.getPassDayOfSeason(date));
//        System.out
//                .println(strDate + " 所在季度剩余多少天？" + QuarterUtils.getRemainDayOfSeason(date));
//        System.out.println(strDate + " 是第几季度？" + QuarterUtils.getSeason(date));
        System.out.println(strDate + " 所在季度月份？"
                + QuarterUtils.formatDate(QuarterUtils.getSeasonDate(date)[0], "yyyy-MM") + "/"
                + QuarterUtils.formatDate(QuarterUtils.getSeasonDate(date)[1], "yyyy年MM月") + "/"
                + QuarterUtils.formatDate(QuarterUtils.getSeasonDate(date)[2], "yyyy年MM月"));
    }

    private static final long nd = 1000 * 24 * 60 * 60;
    private static final long nh = 1000 * 60 * 60;
    private static final long nm = 1000 * 60;

    /**
     * 计算两个时间段时间差，精确到秒
     *
     * @param startTime 2019-04-10 17:16:11
     * @param endTime   2019-04-10 17:28:17
     * @return
     */
    public static String computationTime(Date startTime, Date endTime) {
        return null;
    }

    @Test
    public void dateTest() throws ParseException {
        String startTime = "2021-11-15 12:30:00";
        String endTime = "2021-11-16 13:36:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDate = sdf.parse(endTime);
        Date strDate = sdf.parse(startTime);

        try {
            long diff = endDate.getTime() - strDate.getTime();
            long day = diff / nd;
            long hour = diff % nd / nh;
            long min = diff % nd % nh / nm;
            long sec = diff % nd % nh % nm / 1000;//秒
            String str = null;
            if (day == 0) {
                str = hour + "小时" + min + "分钟";
            } else if (hour == 0) {
                str = min + "分钟";
            } else {
                str = day + "天" + hour + "小时" + min + "分钟";
            }
            System.out.println(hour);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void date() {
        String hour = getLocalDateTimeHour(0);
        System.out.println(hour);
        Date date = Convert.toDate(hour + ":00");

    }


    /**
     * 实时时间格式化到分钟
     *
     * @return
     */
    public static String getLocalDateTimeHour(Integer num) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // 当天日期
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(num);
        // 格式化时间
        String localDateTimeStr = formatter.format(localDateTime);
        return localDateTimeStr;
    }


    @Test
    public void getSing() throws Exception {
//        TreeMap<String, String> requestMap = new TreeMap<>();
//        Map<String, Object> dataMap = new HashMap<>();
//        dataMap.put("shopIds", "1");
//        requestMap.put("method", "api.base.shop.getcommentbyshop");
//        requestMap.put("supplierCode", "100381");
//        requestMap.put("requestId", String.valueOf(UUID.randomUUID()));
//        requestMap.put("version", "1.0");
//        requestMap.put("timestamp", System.currentTimeMillis() + "");
//        requestMap.put("data", JSON.toJSONString(dataMap));
//        String content = JSON.toJSONString(requestMap);
//        String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDAOybRldgmT6rGj2kjatp6dvFBtS4MwIJp1PVTeJegUxjZD/rJF8tXazo/eoPV/mrF7J0ihz/XJxOUH9IRj4iv2Br0HN6uLX5wA932OPgZvSXdHYdssjmIUmKsTFutBZryoImKBRvzR6H/vxQSw34eb413/lHsR+RczHJUFR4inUWKsYEKk5P9VF13dLgn+PHZMGcLTIUNdRnvoUtgO33+hImd3xFEFlG9EMxf/XDrmB8WCzTg011yzwk8VQu8iLBkEaYr6MKQdrvEXbLHcpH6g5SipvUG//YSCcGqX6UoOWRCRR6mVEaPSvkPZk45AcpJyjmF0JD9Maxj4ZBPDxpHAgMBAAECggEACszH94p1pGFhdNply8mbELpR/6EQP0MpkIyYY+IokqdYhAlzy4x8riKmGR7m/UTX9tq+UUxL9+iIHxBSmw33XfnEOebNU/fWXLC67bRxq/CGig+4phC1TsViKc/4bpYcCHmggJKc6WQi4912YT9+fHND/XPaYm3/lH8gBT1SfhtC+WizB6hhWYfn/MLvr+frZgtpumISIkn4+385oq/UzV1EbwCzZzYQ95bEAT1CFy4g4FnU1lbFhqA2G//e66/enmu6yjRy8Dmw3PXI6R0EAoFP0zd5mU+WoGkKRGLGWdw35Ht8zVthCOCjig2LKv3vaVto7a9JKVZLFUXeJuemsQKBgQDf9GPgjeI5d1fgK+kXyk+X5UqvK2hZGV5PFo4MjgVTTV8xQ9Lyb9UppTV1F7uXTS8R6DJVDKUGuJHCLRFlE5dAcN47SgqHtQOM1w9hDNqS2D0UJe/dKSzVkhkFRFJe0m4DntPUbBVhVCDRfc/5W3Z3qg650cZNUYAaDKMpUcglbQKBgQDbvLR/OmeVoLyOxQPgsJDJeir7v13PCjD8P9KBktbzID7x/RRvfK/U9lz34+kt/p8ANrKNN+kZTqERXqp81c2KNg/f1P0o30y6fify+FbXQiqHZzXaxEuNhNSZiSkI1CfdvTX2mMpXGWWkHR9+Z9O+BgUJ/JyhWbBuE+AGJ0kSAwKBgAMx8mbWv16AhGFvXvD28vHA1LLxFJkI0p6gPMCTGiJZHT2PsSfCTrE8aSnVf9ilwkEoJeeuuYACDBpQGEpv4B2MPq9r3ACZpYZR3ydMTqX/rcdmzyhBmSEm53J6yg4ORIGKt4z1SczMrXcvq1LsaiFE7Nbb1k9uFTzJ514Ei611AoGASRNpp4Ih3rX03zoP/xJZJFf3oX9T0D3gapNDC4ps57AKnul+eVErHJTN+DaYXo52DFkkaih04dCvahpkcl2cHGG8R8EKbCRO3RqFaJ1ELBQ6Fouxck6jna3WEjV2KxR7KZf6MUdAy1SL49c3mQ8ocOKJnHehE1vDe+gqMYGxIEcCgYAGv/Hg0qONaRUTLw9X9NqcQ4vLFZ/apx5v5dkUEDviGbsRPWaXfIqWkMuVBzFAlaLjALm+I8xeZ9aLO7/JlEEtzF/VuUDp2xy+w4v2rDGUdNwNV/XtdYZxG4e+5+JFIE84ehcXqsD/kNlfKn4v5+sHlb5kmD8y/38O4XxFhvCACQ==";
//        String sign = RSAUtil.sign(content, privateKey);
//        logger.info("sign=====:" + sign);
//        requestMap.put("sign", sign);
////        String entity = restTemplate.postForObject("https://mkb2test21.mankebao.cn/open/api", JavaUtil.getFormEntity(sign),String.class);
////        entity.replaceAll("\\\\","");
////        JSONObject jsonObject = JSONObject.parseObject(entity);
////        String data1 = jsonObject.getString("data");
//        String str = JSON.toJSONString(requestMap);
//
//        String result = HttpRequest.post("https://mkb2test21.mankebao.cn/open/api")
//                .header("Content-Type", "application/json;charset=utf-8")
//                .timeout(20000)
//                .body(str)
//                .execute()
//                .charset("UTF-8")
//                .body();
//        logger.info("返回回数据：" + result);

    }

    /**
     * redis测试
     */
    @Test
    public void getJedisConn() {
//        final String hots = "10.10.15.250";
//        final int port = 56379;
//        Jedis jedis = new Jedis(hots, port);
//        jedis.set("java", "test");
//        String java = jedis.get("java");
//        System.out.println(java);


    }

    /**
     * get post
     * @throws IOException
     */
    @Test
    public void clz() throws IOException {
//        String url = "http://111.21.122.246:9006/monitor/devices/real/03145851/all/2022-01-10%2011:40:19/2022-01-10%2011:41:20";
//        String request = RequestTools.processHttpRequest(url, "get", new HashMap());
//        logger.info("get:{}", request);

//        String url ="http://47.103.51.48:8090/dnjk/a/interViewTC/sendWarnHandleGroup?username=admin&password=admin";
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("wids", "1");
//        String post = RequestTools.processPostJson(url, jsonObject);
//        logger.info("get:{}", post);

        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        System.out.println(year);
    }


}
