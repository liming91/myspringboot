package com.ming.service;

import com.ming.entities.Product;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Y
* @description 针对表【product】的数据库操作Service
* @createDate 2023-11-28 15:30:19
*/
public interface ProductService extends IService<Product> {

    Product findByIdProduct(String id);

    int updateByIdProduct(Product product);
}
