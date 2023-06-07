package com.ming.controller;

import com.ming.service.ITestService;
import com.ming.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "日期处理")
@RestController
@RequestMapping("/date")
public class DateController {
    @Autowired
    private ITestService iTestService;

    @ApiOperation("测试数据")
    @GetMapping("/test")
    public Result<?> test(int dateType) {
        Map<String, Object> map = iTestService.getList(dateType);
        return Result.success(map);
    }

    @ApiOperation("折线图-根据时间查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateType", value = "时间类型0：24小时 1：月 2：年", required = true, dataType = "string", paramType = "query")
    })
    @GetMapping("/list")
    public Result<?> list(int dateType) {
        Map<String, Object> map = iTestService.list(dateType);
        return Result.success(map);
    }

    @GetMapping("/listTime")
    public Result<?> listTime(int dateType,String startTime, String endTime ) {
        List<Map<String, Object>> map = iTestService.listTime(dateType,startTime,endTime);
        return Result.success(map);
    }

    @GetMapping("/fenTime")
    public Result<?> fenTime(int dateType, String startTime, String endTime) {
        List<Map<String, Object>> map = iTestService.fenTime(dateType, startTime, endTime);
        return Result.success(map);
    }
}
