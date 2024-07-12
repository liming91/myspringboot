package com.ming.mapper;

import com.ming.entities.Info;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;

/**
* @author Y
* @description 针对表【info】的数据库操作Mapper
* @createDate 2023-10-11 14:04:53
* @Entity com.ming.entities.Info
*/
@Mapper
public interface InfoMapper extends BaseMapper<Info> {

    List<Map<String, Object>> getInfoMap();
}




