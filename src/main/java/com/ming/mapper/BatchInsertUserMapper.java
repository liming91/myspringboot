package com.ming.mapper;

import com.ming.entities.HbBaseEnterUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BatchInsertUserMapper{


    int batchInsertEnterUser(@Param("list") List<HbBaseEnterUser> hbBaseEnterUser);

    int  batchUpdateEnterUser(@Param("list") List<HbBaseEnterUser> hbBaseEnterUser);

    int  batchDeleteEnterUser(@Param("list") List<HbBaseEnterUser> enterUserList);
}
