package com.ming.service.impl;

import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import com.ming.entities.TestExcel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: IExcelExportServerImpl
 * @Description: 查询指定页码的数据
 * @Author: WM
 * @Date: 2021-07-24 19:23
 **/
@Service
public class IExcelExportServerImpl implements IExcelExportServer {
    @Override
    public List<Object> selectListForExcelExport(Object obj, int page) {
        // 在数据库中查询出数据集合
        //此处可以写dao层分页查询的实现方法，page为当前第几页，分批次循环查询导入数据至excel中
        //obj:查询条件，page：当前第几页，pageSize：每页条数
        //List<Test> testList = dao.xxx(obj,page,pageSize);
        int pageSize = 100;
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {  //测试数据
            TestExcel testExcel = new TestExcel();
            testExcel.setId("1" + i);
            testExcel.setName("子龙" + i);
            testExcel.setBirthday(new Date());
            testExcel.setPhone("1320000" + i);
            testExcel.setRemark("备注" + i);
            list.add(testExcel);
        }
        List partList = new ArrayList();
        if (page * pageSize <= 10000) {
            partList = list.subList((page - 1) * pageSize, page * pageSize);
        }
        return partList;
    }
}
