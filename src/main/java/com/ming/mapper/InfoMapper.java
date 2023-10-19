package com.ming.mapper;

import com.ming.entities.Info;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;

/**
* @author Y
* @description 针对表【info】的数据库操作Mapper
* @createDate 2023-10-11 14:04:53
* @Entity com.ming.entities.Info
*/
@Mapper
public interface InfoMapper extends BaseMapper<Info> {

}




