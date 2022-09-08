package com.ming.service.impl;

import com.alibaba.druid.sql.PagerUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ming.bean.User;
import com.ming.mapper.UserMapper;
import com.ming.service.IUserService;
import com.ming.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectList(null);
        PageInfo<User> pageInfo = new PageInfo<>(list);
        PageResult pageResult = PageResult.getPageResult(pageInfo);
        return pageResult;
    }
}
