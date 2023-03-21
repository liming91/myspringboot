package com.ming.service;

import javax.servlet.http.HttpServletResponse;

public interface ExcelService {

    public void export(HttpServletResponse response);

    void exportSheet(HttpServletResponse response);

    String importTemplate(HttpServletResponse response);
}
