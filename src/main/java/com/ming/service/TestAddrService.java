package com.ming.service;

import com.ming.entities.TestAddr;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.entities.VO.TestAddrVO;

import java.util.List;

/**
* @author Y
* @description 针对表【test_addr】的数据库操作Service
* @createDate 2024-06-26 14:19:51
*/
public interface TestAddrService extends IService<TestAddr> {

    List<TestAddrVO> infoList();
}
