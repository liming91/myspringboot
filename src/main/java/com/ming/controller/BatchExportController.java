package com.ming.controller;

import com.ming.service.IAsyncBatchExportExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * excel批量导出测试
 *
 * @Author liming
 * @Date 2024/8/9 11:03
 */

@Api(tags = "excel批量导出测试")
@Slf4j
@RestController
@RequestMapping("/batchExcel")
@RequiredArgsConstructor
public class BatchExportController {

    private final IAsyncBatchExportExcelService iAsyncBatchExportExcelService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", paramType = "query")
    })
    @ApiOperation("批量导出")
    @GetMapping("/export")
    public void export() {
        Map<String, Object> map = new HashMap<>();
        map.put("page",1);
        map.put("limit",10000);
        CountDownLatch countDownLatch = new CountDownLatch(50);
        //iAsyncBatchExportExcelService.executeAsyncTask(map,countDownLatch);
    }

}
