package com.ming.mp;

import com.ming.bean.User;
import com.ming.mapper.UserMapper;
import com.ming.service.IUserService;
import com.ming.util.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
public class MpTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    IUserService userService;
    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------")+userMapper);
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    public void testPage(){
        PageResult userServicePage = userService.findPage(1, 10);
        System.out.println(userServicePage);
    }
}
