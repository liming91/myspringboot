package com.ming.service;


import com.ming.entities.HbBaseEnterUser;

import java.util.List;

public interface IHbBaseEnterUserService {

    /**
     * 企业授权
     * @param hbBaseEnterUser
     * @return
     */
    int addGrantEnter(List<HbBaseEnterUser> hbBaseEnterUser);
}
