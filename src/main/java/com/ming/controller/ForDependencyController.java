package com.ming.controller;

import com.ming.service.IForDependService1;
import com.ming.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author liming
 * @Date 2023/10/20 16:02
 */
@Api(tags = "循环依赖测试")
@RestController
@RequestMapping("/depend")
public class ForDependencyController {

    @Autowired
    private IForDependService1 iForDependService1;

    @ApiOperation("测试")
    @GetMapping("/depend")
    public Result<?> depend() {
         iForDependService1.getDependService();
        return Result.success();
    }
}
