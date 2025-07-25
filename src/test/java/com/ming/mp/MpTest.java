package com.ming.mp;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.ming.bean.User;
import com.ming.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
public class MpTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------")+userMapper);
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    public static void main(String[] args) {
        String format = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);
        System.out.println(format);
    }
}
