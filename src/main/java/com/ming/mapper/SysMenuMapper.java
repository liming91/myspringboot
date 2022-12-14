package com.ming.mapper;

import com.ming.entities.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.ming.entities.SysMenu
 */
@Mapper
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

}




