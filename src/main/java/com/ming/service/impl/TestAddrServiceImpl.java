package com.ming.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.entities.TestAddr;
import com.ming.entities.VO.TestAddrVO;
import com.ming.service.TestAddrService;
import com.ming.mapper.TestAddrMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Y
 * @description 针对表【test_addr】的数据库操作Service实现
 * @createDate 2024-06-26 14:19:51
 */
@Service
public class TestAddrServiceImpl extends ServiceImpl<TestAddrMapper, TestAddr>
        implements TestAddrService {

    @Override
    public List<TestAddrVO> infoList() {
        List<TestAddrVO> list = this.baseMapper.infoList();
        list.forEach(x->{
            TestAddr addr = this.getById(x.getId());
            x.setAddr(addr);
        });
        return list;
    }
}




