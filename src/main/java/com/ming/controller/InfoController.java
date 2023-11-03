package com.ming.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.annotation.NeedEncrypt;
import com.ming.entities.Info;
import com.ming.enums.ResultCode;
import com.ming.service.InfoService;
import com.ming.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/info")
@Api(tags = "信息")
public class InfoController {

    private final InfoService infoService;


    @ApiOperation("信息列表")
    @GetMapping("/userPage")
    public Result<?> userPage(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Info> list = infoService.infoPage(pageNo, pageSize);
        return Result.success(list);
    }

    /**
     * 添加时name为唯一索引
     * @param info
     * @return
     */

    @ApiOperation("添加")
    @PostMapping("/save")
    @NeedEncrypt
    public Result<?> save(@RequestBody Info info){
        boolean flag =  infoService.saveOrUpdateInfo(info);
        if(flag){
            return Result.success(ResultCode.E02);
        }
        return Result.failure(ResultCode.E03);
    }

    /**
     * 逻辑删除后，如何保证数据库某一个字段的唯一性
     * 新增时为删除的字段的名称一样违法唯一索引

     * @param id
     * @return
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    public Result delete(String id) {
        ResultCode resultCode = infoService.updateInfo(id);
        return Result.success(resultCode);
    }

}
