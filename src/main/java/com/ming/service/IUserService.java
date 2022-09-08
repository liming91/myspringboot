package com.ming.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.bean.User;
import com.ming.util.PageResult;

public interface IUserService extends IService<User> {

    PageResult findPage(int pageNum,int pageSize);
}
