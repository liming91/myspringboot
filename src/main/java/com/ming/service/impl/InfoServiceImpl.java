package com.ming.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.entities.Info;
import com.ming.service.InfoService;
import com.ming.mapper.InfoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author Y
* @description 针对表【info】的数据库操作Service实现
* @createDate 2023-10-11 14:04:53
*/
@Service
@AllArgsConstructor
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info>
    implements InfoService{

    @Override
    public IPage<Info> infoPage(Integer pageNo, Integer pageSize) {
        Page<Info> page = new Page<>(pageNo,pageSize);
        Page<Info> infoPage = this.baseMapper.selectPage(page, new LambdaQueryWrapper<>());
        return infoPage;
    }
}




