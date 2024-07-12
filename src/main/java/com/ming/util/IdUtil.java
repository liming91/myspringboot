package com.ming.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class IdUtil {
    private final JdbcTemplate jdbcTemplate;


    private final RedisTemplate redisTemplate;


    public IdUtil(JdbcTemplate jdbcTemplate, RedisTemplate redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
    }

    /**
     * String.format 是一个静态方法，用于创建一个格式化的字符串。
     * "%1$02d" 是格式化字符串的模板。在这里：
     * %1$ 表示第一个参数的索引位置。在这个例子中，参数 6 是唯一的参数，所以 %1$ 表示参数 6。
     * 02d 是格式化说明符，指定了如何格式化参数。在这里：
     * 0 表示使用零填充。
     * 2 表示最小宽度为2个字符。
     * d 表示参数是一个十进制整数。
     * 综合起来，"%1$02d" 的意思是：将第一个参数（在这里是整数 6）格式化为至少两位宽度的十进制整数，不足两位的部分用零填充。因此，6 格式化后会变成 "06"。
     * 以天为单位逐步递增  目前暂支持日增长量在万级别的
     *
     * @param tableName 表名
     * @return
     */
    public Long orderIdIncrea(String tableName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //补两位,因为一年最多三位数
        String monthFormat = String.format("%1$02d", month + 1);
        //补两位，因为日最多两位数
        String dayFormat = String.format("%1$02d", day);
        String orderIdPrefix = year + monthFormat + dayFormat;
        String key = "ID::" + tableName + "_" + orderIdPrefix;
        String orderId = null;
        try {
            Long increment = redisTemplate.opsForValue().increment(key, 1);
            redisTemplate.expire(key, 1, TimeUnit.DAYS);
            //如果id序号为1,防止redis重启造成的缓存失效,后续的id冲突问题,查库最大的id
            String selectSql = " SELECT MAX(num) MAXID FROM " + tableName;
            System.out.println(selectSql);
            if (increment == 1L) {
                String maxID = jdbcTemplate.queryForObject(selectSql, String.class);
                if (StringUtils.isNotEmpty(maxID)) {
                    //判断是否包含当前时间节点,包含则说明今天存在过工单,拿到他的序列号
                    String[] idSplit = maxID.split(orderIdPrefix);
                    if (idSplit.length >= 2) {  //正常肯定等于1 ,异常情况大于等于2,说明今天生成过id
                        Integer num = Integer.parseInt(idSplit[1]);
                        increment = redisTemplate.opsForValue().increment(key, num);
                    }
                }
            }
            //往前补5位
            orderId = orderIdPrefix + String.format("%1$05d", increment);
        } catch (Exception e) {
//            logger.error("生成ID号失败",e);
        }
        return Long.valueOf(orderId);
    }

    /**
     * 工单编号生成规则,当天递增
     *
     * @param tableName
     * @return
     */
    public synchronized Long orderIdNum(String tableName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //补两位,因为一年最多三位数
        String monthFormat = String.format("%1$02d", month + 1);
        //补两位，因为日最多两位数
        String dayFormat = String.format("%1$02d", day);
        String orderIdPrefix = year + monthFormat + dayFormat;
        String key = "orderID::" + tableName + "_" + orderIdPrefix;
        String orderId = null;
        try {
            Long increment = redisTemplate.opsForValue().increment(key, 1);
            redisTemplate.expire(key, 1, TimeUnit.DAYS);
            //如果id序号为1,防止redis重启造成的缓存失效,后续的id冲突问题,查库最大的id
            String selectSql = " SELECT MAX(ID) MAXID FROM " + tableName;
            System.out.println(selectSql);
            String maxID = jdbcTemplate.queryForObject(selectSql, String.class);
            System.out.println(maxID);
            if (StringUtils.isNotEmpty(maxID) && maxID.contains(orderIdPrefix)) {  //如果包含当前日期的则拿到值进行相加
                maxID = maxID.split("-")[0];
                String[] idSplit = maxID.split(orderIdPrefix);
                if (idSplit.length >= 2) {  //第一次则为1,
                    Integer num = Integer.parseInt(idSplit[1]);
                    increment += num;
                }
            }
            //往前补5位
            orderId = orderIdPrefix + String.format("%1$05d", increment);
        } catch (Exception e) {
            log.error("生成ID号失败", e);
        }
        assert orderId != null;
        return Long.valueOf(orderId);
    }


}
