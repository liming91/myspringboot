package com.ming.mapper;

import com.ming.entities.TestAddr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ming.entities.VO.TestAddrVO;

import java.util.List;

/**
* @author Y
* @description 针对表【test_addr】的数据库操作Mapper
* @createDate 2024-06-26 14:19:51
* @Entity com.ming.entities.TestAddr
*/
public interface TestAddrMapper extends BaseMapper<TestAddr> {

    List<TestAddrVO> infoList();
}




