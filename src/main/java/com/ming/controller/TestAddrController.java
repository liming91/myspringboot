package com.ming.controller;

import com.ming.bean.Order;
import com.ming.bean.PayResult;
import com.ming.entities.TestAddr;
import com.ming.entities.VO.TestAddrVO;
import com.ming.enums.ResultCode;
import com.ming.service.TestAddrService;
import com.ming.strategy.IStrategyService;
import com.ming.strategy.PayContext;
import com.ming.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author liming
 * @Date 2024/6/26 14:20
 */
@Api(value = "json添加测试")
@RestController
@RequestMapping("/testAddr")
@RequiredArgsConstructor
public class TestAddrController {


    private final TestAddrService testAddrService;

    @ApiOperation("添加")
    @PostMapping("/add")
    public Result<?> add(@RequestBody TestAddr testAddr) {
        boolean flag = testAddrService.save(testAddr);
        if (flag) {
            return Result.success(ResultCode.E02);
        }
        return Result.failure(ResultCode.E03);
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public Result<?> list() {
        List<TestAddr> list = testAddrService.list();
        return Result.success(list);
    }


    @ApiOperation("关联列表")
    @GetMapping("/infoList")
    public Result<?> infoList() {
        List<TestAddrVO> list = testAddrService.infoList();
        return Result.success(list);
    }

}
