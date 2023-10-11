package com.ming.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.entities.Info;
import com.ming.service.InfoService;
import com.ming.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/info")
@Api(tags = "信息")
public class InfoController {

 private final InfoService infoService;


    @ApiOperation("信息列表")
    @GetMapping("/userPage2")
    public Result<?> userPage2(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Info> list = infoService.infoPage(pageNo, pageSize);
        return Result.success(list);
    }


}
