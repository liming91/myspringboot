package com.ming.controller;

import com.ming.bean.GenerateResult;
import com.ming.bean.MessageEnum;
import com.ming.bean.Result;
import com.ming.service.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/excel")
@Slf4j
public class ExcelController {


    @Autowired
    private ExcelService excelService;

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        excelService.export(response);
    }


    @GetMapping("/exportSheet")
    public void exportSheet(HttpServletResponse response) {
        excelService.exportSheet(response);
    }

    @PostMapping("/importTemplate")
    public Result importTemplate(HttpServletResponse response) {
        String url = excelService.importTemplate(response);
        return GenerateResult.genDataSuccessResult(url);
    }
}
