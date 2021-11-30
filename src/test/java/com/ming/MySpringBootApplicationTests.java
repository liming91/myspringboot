package com.ming;

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
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MySpringBootApplicationTests {

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
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
