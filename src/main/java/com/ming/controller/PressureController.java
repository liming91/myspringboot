package com.ming.controller;

import com.ming.entities.Product;
import com.ming.enums.ResultCode;
import com.ming.service.ProductService;
import com.ming.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

/**
 * 悲观锁和乐观锁介绍以及相应测试
 *
 * @Author liming
 * @Date 2023/11/28 15:34
 */
@RestController
@RequestMapping("/product")
@AllArgsConstructor
@Api(tags = "悲观锁和乐观锁介绍以及相应测试")
public class PressureController {

    private final ProductService productService;


    /**
     * @param product
     * @return
     */
    @Transactional
    @ApiOperation("库存扣减")
    @PostMapping("/update")
    public Result<?> save(@RequestBody Product product) {
        //购买商品的时候，减去相应库存，注意这里减库存默认为-1
        Product prod = productService.findByIdProduct(product.getId());
        prod.setNum(prod.getNum() - 1);
        int rows = productService.updateByIdProduct(prod);
        if (rows > 0) {
            return Result.success(ResultCode.E05);
        }
        return Result.failure(ResultCode.E06);
    }
}
