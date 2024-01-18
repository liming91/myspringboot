package com.ming.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ming.bean.Test;
import com.ming.entities.VO.DataTrendListVo;
import com.ming.entities.VO.DataTrendVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TestMapper  extends BaseMapper<Test> {
    public int addTest(@Param("list") List<Test> testList);

    List<Test> getDateByTime(@Param("queryDate") String queryDate, @Param("resDate") String resDate, @Param("localDate") String localDate);

    List<Test> select();

    List<Test> getList(@Param("queryDate") String queryDate, @Param("resDate") String resDate, @Param("localDate") String localDate);

    List<DataTrendVO> findSevenDate();
}
