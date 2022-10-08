package com.ming.mapper;

import com.ming.entities.VO.InFoVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author liming
 * @Date 2022/10/8 16:36
 */
@Mapper
@Repository
public interface InfoMapper {

    List<InFoVO> getInfo();
}
