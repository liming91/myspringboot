package com.ming.controller;

import com.ming.bean.User;
import com.ming.service.IUserService;
import com.ming.util.Page;
import com.ming.util.PageInfoUtil;
import com.ming.util.PageResult;
import org.assertj.core.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService userService;

    @RequestMapping("/findUser")
    public Object findUser(Integer pageNum,Integer pageSize){

        return userService.findPage(pageNum, pageSize);
    }

    @RequestMapping("/pageUser")
    public Map<String,Object> pageUser(Integer pageNum, Integer pageSize){
        List<User> list = userService.list();
        Page<User> pageInfoUtil = new Page<>(list,pageNum,pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("data",pageInfoUtil);
        return map;
    }
}
