package com.ming.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ming.entities.HbBaseEnterUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BatchInsertUserMapper extends BaseMapper<HbBaseEnterUser> {


    int batchInsertEnterUser(@Param("list") List<HbBaseEnterUser> hbBaseEnterUser);

    int  batchUpdateEnterUser(@Param("list") List<HbBaseEnterUser> hbBaseEnterUser);

    int  batchDeleteEnterUser(@Param("list") List<HbBaseEnterUser> enterUserList);
}
