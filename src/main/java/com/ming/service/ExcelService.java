package com.ming.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExcelService {

    public void export(HttpServletResponse response);

    void exportSheet(HttpServletResponse response);

    String importTemplate(HttpServletResponse response);

    String importTest(MultipartFile file) throws Exception;
}
