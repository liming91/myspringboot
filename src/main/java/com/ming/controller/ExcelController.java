package com.ming.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.ming.bean.Test;
import com.ming.service.ExcelService;
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

    @PostMapping("/food/import")
    @ApiOperation(value = "批量导入Excel")
    public String save(@RequestParam("file") MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        List<Test> result = ExcelImportUtil.importExcel(file.getInputStream(),Test.class, params);
        //将图片上传到FastDFS并且将地址保存
        result.forEach(test->{
            if(StringUtils.isNotEmpty(test.getName())){
                try {
                    File files = new File(test.getImage());
                    FileInputStream fileInputStream = new FileInputStream(files);
                    MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(fileInputStream));
                    String imgUrl = null;
                    test.setImage(imgUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //业务层保存数据

        return "success";
    }


    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        excelService.export(response);
    }


    @GetMapping("/exportSheet")
    public void exportSheet(HttpServletResponse response) {
        excelService.exportSheet(response);
    }

    @GetMapping("/importTemplate")
    @ApiOperation(value = "导出模版")
    public Result<?> importTemplate(HttpServletResponse response) {
        String url = excelService.importTemplate(response);
        return Result.success(url);
    }
}
