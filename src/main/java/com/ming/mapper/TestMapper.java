package com.ming.mapper;

import com.ming.bean.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TestMapper {
    public int addTest(@Param("list") List<Test> testList);

    List<Test> select();

    List<Test> getList(@Param("queryDate") String queryDate, @Param("resDate") String resDate, @Param("localDate") String localDate);
}
