package com.ming.controller;

import com.ming.bean.GenerateResult;
import com.ming.bean.Result;
import com.ming.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/date")
public class DateController {
    @Autowired
    private ITestService iTestService;

    @GetMapping("/test")
    public Result<?> test(int dateType) {
        Map<String, Object> map = iTestService.getList(dateType);
        return GenerateResult.genDataSuccessResult(map);
    }
}
