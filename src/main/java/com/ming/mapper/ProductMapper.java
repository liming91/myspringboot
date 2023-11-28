package com.ming.mapper;

import com.ming.entities.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @author Y
* @description 针对表【product】的数据库操作Mapper
* @createDate 2023-11-28 15:30:19
* @Entity com.ming.entities.Product
*/
@Mapper
@Repository
public interface ProductMapper extends BaseMapper<Product> {

    Product findByIdProduct(String id);

    int updateByIdProduct(@RequestParam("product") Product product);
}




