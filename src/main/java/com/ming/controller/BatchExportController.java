package com.ming.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import com.ming.entities.TestExcel;
import com.ming.service.IAsyncBatchExportExcelService;
import com.ming.util.excel.AsyncExcelExportUtil;
import com.ming.util.excel.MyExcelExportUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
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

    @Resource
    private  IExcelExportServer exportBigExcel;

    private final MyExcelExportUtil myExcelExportUtil;

    private final AsyncExcelExportUtil asyncExcelExportUtil;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", paramType = "query")
    })
    @ApiOperation("利用CountDownLatch导出数据")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        asyncExcelExportUtil.threadExcel(response);
        //1000000-39511ms 100000-7750ms 10000-789ms
    }


    /**
     * 普通数据量导出(修改表格宽度测试)
     */
    @GetMapping(value = "/export2")
    public void test2() {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {  //测试数据
            TestExcel testExcel = new TestExcel();
            testExcel.setId("1"+i);
            testExcel.setName("子龙"+i);
            testExcel.setBirthday(new Date());
            testExcel.setPhone("1320000"+i);
            testExcel.setRemark("备注"+i);
            list.add(testExcel);
        }
        try {
            long start = System.currentTimeMillis();
            Workbook workbook = myExcelExportUtil.getWorkbook("计算机一班学生", "学生", TestExcel.class, list, ExcelType.XSSF);
            String filePath = "F:\\excel\\upload";
            File file = new File(filePath);
            MyExcelExportUtil.exportExcel2(workbook, file);
            long end = System.currentTimeMillis();
            log.info("任务执行完毕共消耗：  " + (end - start) + "ms");
            //1000000-69407ms  100000-3518ms 10000-1310ms
        } catch (Exception e) {
            log.error("普通数据量导出异常！", e);
        }
    }

    /**
     * 大数据量导出测试
     *
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/bigDataExport")
    public void bigDataExport(HttpServletResponse response) throws IOException {
        Date start = new Date();
        ExportParams params = new ExportParams("大数据测试", "测试");
        Workbook workbook = ExcelExportUtil.exportBigExcel(params, TestExcel.class, exportBigExcel, new Object());
        MyExcelExportUtil.exportExcel(workbook, String.valueOf(System.currentTimeMillis()), response);
        log.info("bigDataExport:" + (new Date().getTime() - start.getTime()));
        //10000-bigDataExport:2278 100000-bigDataExport:19083 1000000-bigDataExport:693672
    }

    /**
     * 普通数据量导出
     *
     * @param response
     */
    @GetMapping(value = "/ordinaryExport")
    public void ordinaryExport(HttpServletResponse response) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {  //一百万数据量
            TestExcel testExcel = new TestExcel();
            testExcel.setId("1"+i);
            testExcel.setName("子龙"+i);
            testExcel.setBirthday(new Date());
            testExcel.setPhone("1320000"+i);
            testExcel.setRemark("备注"+i);
            list.add(testExcel);
        }
        try {
            Date start = new Date();
            Workbook workbook = myExcelExportUtil.getWorkbook("大数据测试", "测试", TestExcel.class, list, ExcelType.XSSF);
            MyExcelExportUtil.exportExcel(workbook, String.valueOf(System.currentTimeMillis()), response);
            log.info("export:" + (new Date().getTime() - start.getTime()));
            //10000-export:1208 100000-export:4516 1000000-export:329188
        } catch (Exception e) {
            log.error("普通数据量导出异常！", e);
        }
    }

}
