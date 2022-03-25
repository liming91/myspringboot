package com.ming.controller;

import com.ming.service.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

}
