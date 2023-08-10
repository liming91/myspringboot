package com.ming;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.sql.*;

/**
 * @Author liming
 * @Date 2023/8/10 15:03
 */
public class JdbcTest {

    @Test
    public void test1() {
        System.out.println("测试");
        //1、定义驱动
        String driver = "com.mysql.cj.jdbc.Driver";//数据库驱动类所对应的字符串
        String url = "jdbc:mysql://43.142.146.167:3306/db?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false";
        String userName = "root";
        String pwd = "724933";
        Connection conn = null;

        try {
            //2、加载驱动
            Class.forName(driver);
            //3、通过驱动获取数据库连接
            conn = DriverManager.getConnection(url, userName, pwd);
            //4、定义sql
            String sql = "select * from test";
            //5、步骤五:创建一个PreparedStatement对象(可以用来执行sql,来操作数据库)
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            //6、执行数据库,得到结果集
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                System.out.println("姓名：" + name);
            }
            //7、关闭连接
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("驱动程序配置未配置成功");
        } catch (SQLException e) {
            System.out.println("数据库连接失败!!!");

        }
    }
}
