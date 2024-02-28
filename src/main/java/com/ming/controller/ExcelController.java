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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import java.util.Map;
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
    public void exportList(HttpServletResponse response, String startTime, String endTime) throws IOException {
        List<Test> list = iTestService.getList();
        Map<String, List<Test>> collect = list.stream().collect(Collectors.groupingBy(Test::getName));
        String title = "测试导出.xls";
        if (StringUtils.isNotBlank(startTime)) {
            title = startTime + "-" + endTime + title;
        }
        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyleUtil.class);
        exportParams.setHeight((short) 15);

        Workbook result = ExcelExportUtil.exportExcel(exportParams, Test.class, list);

        //再数据末尾填需要额外提示的提示内容
        Sheet sheet = result.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        Row row1 = sheet.getRow(lastRowNum + 3);
        Row row2 = sheet.getRow(lastRowNum + 4);
        ExcelStyleUtil excelStyleUtil = new ExcelStyleUtil(result);

        int num = 1;
        for (Map.Entry<String, List<Test>> entry : collect.entrySet()) {
            if (null == row1) {
                row1 = sheet.createRow(lastRowNum + 3);
                Cell cell = row1.createCell(0);
                cell.setCellStyle(excelStyleUtil.buildCellStyle(result, ExcelStyleUtil.REMARK_STYLES));
            }
            Cell cell1 = row1.createCell(num);
            cell1.setCellValue(entry.getKey());
            cell1.setCellStyle(excelStyleUtil.buildCellStyle(result, ExcelStyleUtil.DATA_STYLES));

            if (null == row2) {
                row2 = sheet.createRow(lastRowNum + 4);
                Cell cell = row2.createCell(0);
                cell.setCellValue("合计");
                cell.setCellStyle(excelStyleUtil.buildCellStyle(result, ExcelStyleUtil.REMARK_STYLES));
            }
            Cell cell = row2.createCell(num);
            cell.setCellValue(entry.getValue().size());
            cell.setCellStyle(excelStyleUtil.buildCellStyle(result, ExcelStyleUtil.DATA_STYLES));
            num++;
        }


        //设置请求头,解决文件名中文乱码问题
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;fileName=" + URLEncoder.encode(title, "utf-8"));
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
