package com.ming.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.annotation.NeedDecrypt;
import com.ming.annotation.NeedEncrypt;
import com.ming.config.CustomIdGenerator;
import com.ming.entities.Info;
import com.ming.enums.ResultCode;
import com.ming.service.InfoService;
import com.ming.util.IdUtil;
import com.ming.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;



@Slf4j
@RestController

@RequestMapping("/info")
@Api(tags = "信息")
public class InfoController {

    @Autowired
    private InfoService infoService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate redisTemplate;


    @ApiOperation("id测试")
    @GetMapping("/getId")
    public Result<?> getId() {
        IdUtil idUtil = new IdUtil(jdbcTemplate,redisTemplate);
        String id =  "ID" + idUtil.orderIdIncrea("info");

        return Result.success(id);
    }

    @ApiOperation("id测试")
    @GetMapping("/getWoId")
    public Result<?> getWoId() {
        IdUtil idUtil = new IdUtil(jdbcTemplate,redisTemplate);
        String id =  "W0" + idUtil.orderIdNum("info");
        return Result.success(id);
    }



    @ApiOperation("id测试")
    @GetMapping("/getIdtwo")
    public Result<?> getIdtwo() {
        CustomIdGenerator customIdGenerator = new CustomIdGenerator();

        String s = customIdGenerator.nextUUID("info");
        return Result.success(s);
    }

    @NeedDecrypt()
    @ApiOperation("信息列表")
    @GetMapping("/list")
    public Result<?> list() {
        List<Info> list = infoService.list();
        return Result.success(list);
    }


    @NeedDecrypt()
    @ApiOperation("信息列表分页")
    @GetMapping("/userPage")
    public Result<?> userPage(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Info> list = infoService.infoPage(pageNo, pageSize);
        return Result.success(list);
    }

    /**
     * 添加时name为唯一索引
     *
     * @param info
     * @return
     */

    @ApiOperation("添加")
    @PostMapping("/save")
    //@NeedEncrypt
    public Result<?> save(@Validated @RequestBody Info info) {
        boolean flag = infoService.saveOrUpdateInfo(info);
        if (flag) {
            return Result.success(ResultCode.E02);
        }
        return Result.failure(ResultCode.E03);
    }


    @ApiOperation("批量添加")
    @PostMapping("/batchSave")
    @NeedEncrypt
    public Result<?> batchSave(@RequestBody List<Info> info) {
        for (int i = 0; i < info.size(); i++) {
            //批量插入时间会一致，解决按时间排序问题
            info.get(i).setTime(new Date(System.currentTimeMillis() + i));
        }
        boolean flag = infoService.saveOrUpdateBatch(info);
        if (flag) {
            return Result.success(ResultCode.E02);
        }
        return Result.failure(ResultCode.E03);
    }

    /**
     * 逻辑删除后，如何保证数据库某一个字段的唯一性
     * 新增时为删除的字段的名称一样违法唯一索引
     *
     * @param id
     * @return
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    public Result delete(String id) {
        ResultCode resultCode = infoService.updateInfo(id);
        return Result.success(resultCode);
    }


    /**
     * 更新info造成死锁
     */
    @ApiOperation("更新")
    @GetMapping ("/changeInfo")
    public void lockCar() {
         infoService.changeInfo();
    }

}
