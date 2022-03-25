package com.ming.service.impl;

import com.ming.bean.Test;
import com.ming.mapper.TestMapper;
import com.ming.service.ExcelService;
import com.ming.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;
import java.util.List;
@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    private TestMapper testMapper;

    @Override
    public void export(HttpServletResponse response) {
        // 模拟从数据库获取需要导出的数据
        List<Test> personList = testMapper.select();
        //导出操作
        ExcelUtil.exportExcel(personList, "用户列表", "用户", Test.class, "用户信息" + new Date() + ".xls", response);

    }
}
