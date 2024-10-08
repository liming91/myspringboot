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
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
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
    private IExcelExportServer exportBigExcel;

    private final MyExcelExportUtil myExcelExportUtil;

    private final AsyncExcelExportUtil asyncExcelExportUtil;

    @Value("${file.upload-dir}")
    private String uploadDir;

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
        for (int i = 0; i < 1000; i++) {  //测试数据
            TestExcel testExcel = new TestExcel();
            testExcel.setId("1" + i);
            testExcel.setName("子龙" + i);
            testExcel.setBirthday(new Date());
            testExcel.setPhone("1320000" + i);
            testExcel.setRemark("备注" + i);
            list.add(testExcel);
        }
        try {
            long start = System.currentTimeMillis();
            Workbook workbook = myExcelExportUtil.getWorkbook("测试", "test", TestExcel.class, list, ExcelType.XSSF);
            String filePath = "F:\\excel\\upload.xlsx";

            //String filePath = "/home/excel/upload.xlsx";
            File file = new File(uploadDir+"/upload.xlsx");
            MyExcelExportUtil.exportExcel2(workbook, file);
            long end = System.currentTimeMillis();
            log.info("任务执行完毕共消耗：  " + (end - start) + "ms");
            //1000000-69407ms  100000-3518ms 10000-1310ms
        } catch (Exception e) {
            e.printStackTrace();
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
        String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        log.info("字体检查"+"Found " + names.length + " fonts:");
        Date start = new Date();
        ExportParams params = new ExportParams("大数据量导出大数据测试", "测试大数据量导出");
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
        for (int i = 0; i < 1000; i++) {
            //TODO 一百万数据量 因为云服务器内存2G改为1000条数据
            TestExcel testExcel = new TestExcel();
            testExcel.setId("1" + i);
            testExcel.setName("子龙" + i);
            testExcel.setBirthday(new Date());
            testExcel.setPhone("1320000" + i);
            testExcel.setRemark("备注" + i);
            list.add(testExcel);
        }
        try {
            Date start = new Date();
            Workbook workbook = myExcelExportUtil.getWorkbook("普通大数据量导出大数据测试", "测试普通大数据量导出", TestExcel.class, list, ExcelType.XSSF);
            MyExcelExportUtil.exportExcel(workbook, String.valueOf(System.currentTimeMillis()), response);
            log.info("export:" + (new Date().getTime() - start.getTime()));
            //10000-export:1208 100000-export:4516 1000000-export:329188
        } catch (Exception e) {
            e.printStackTrace();
            log.error("普通数据量导出异常！", e);
        }
    }

}
