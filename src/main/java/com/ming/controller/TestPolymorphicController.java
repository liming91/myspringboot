package com.ming.controller;

import com.ming.annotation.NeedDecrypt;
import com.ming.entities.Info;
import com.ming.entities.PolymorphicTest;
import com.ming.service.IPolymorphicService;
import com.ming.service.InfoService;
import com.ming.service.SysUserService;
import com.ming.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * @Author liming
 * @Date 2024/7/4 15:47
 */
@Api(value = "polymorphic")
@RestController
@RequiredArgsConstructor
@RequestMapping("/polymorphic")
public class TestPolymorphicController {

    private final InfoService infoService;

    private final SysUserService sysUserService;

    @NeedDecrypt()
    @ApiOperation("信息列表")
    @GetMapping("/infoList")
    public Result<?> infoList() throws ExecutionException, InterruptedException {
        List<Map<String, Object>> list = Collections.synchronizedList(new ArrayList<>());
        CompletableFuture<Void> task1 = CompletableFuture.runAsync(new PolymorphicTest(list, infoService));
        CompletableFuture<Void> task2 = CompletableFuture.runAsync(new PolymorphicTest(list, sysUserService));
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(task1, task2);
        completableFuture.get();
        return Result.success(list);
    }

}
