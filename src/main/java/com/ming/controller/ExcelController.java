package com.ming.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.ming.bean.Test;
import com.ming.service.ExcelService;
import com.ming.service.ITestService;
import com.ming.util.ExcelStyleUtil;
import com.ming.util.ExcelUtil;
import com.ming.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/excel")
@Slf4j
@Api(tags = "excel")
public class ExcelController {


    @Autowired
    private ExcelService excelService;



    @Autowired
    private ITestService iTestService;

    @ApiOperation(value = "Excel模板下载")
    @GetMapping("/importDownload")
    public void importExcel(HttpServletResponse response) {
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("标题名", "sheet名"), Test.class, new ArrayList<Test>());
        try {
            response.setContentType("application/vnd.ms-excel");
            String fileName = URLEncoder.encode("文件名.xlsx", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ";" + "filename*=utf-8''" + fileName);
            // 服务端要在header设置Access-Control-Expose-Headers, 前端才能正常获取到
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            OutputStream output = response.getOutputStream();
            workbook.write(output);
            output.flush();
            output.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        excelService.export(response);
    }

    @GetMapping("/exportList")
    public void exportList(HttpServletResponse response,String startTime,String endTime) throws IOException {
        List<Test> list = iTestService.getList();
        String title = "测试导出.xls";
        if(StringUtils.isNotBlank(startTime)){
            title = startTime + "-" + endTime + title;
        }
        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyleUtil.class);
        exportParams.setHeight((short) 15);

        Workbook result = ExcelExportUtil.exportExcel(exportParams, Test.class, list);
        //设置请求头,解决文件名中文乱码问题
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-disposition","attachment;fileName="+ URLEncoder.encode(title,"utf-8"));
        result.write(response.getOutputStream());
        // 清除response
        response.flushBuffer();
        result.close();
    }



    @GetMapping("/exportSheet")
    public void exportSheet(HttpServletResponse response) {
        excelService.exportSheet(response);
    }


    @PostMapping("/import")
    @ApiOperation(value = "导入")
    public Result<?> importTest(@RequestParam("file") MultipartFile file) throws Exception {
        String s = excelService.importTest(file);
        return Result.success(s);
    }

    @GetMapping("/importTemplate")
    @ApiOperation(value = "导出模版")
    public Result<?> importTemplate(HttpServletResponse response) {
        String url = excelService.importTemplate(response);
        return Result.success(url);
    }
}
