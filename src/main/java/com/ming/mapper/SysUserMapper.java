package com.ming.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.entities.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户
 * @Author liming
 * @Date 2023/3/31 11:17
 */
@Mapper
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
    IPage<SysUser> userPage(Page<SysUser> page,String keywords);
}
