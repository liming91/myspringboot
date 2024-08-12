package com.ming.service.impl;

import com.ming.entities.TestExcel;
import com.ming.service.IAsyncBatchExportExcelService;
import com.ming.util.excel.MyExcelExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.scheduling.annotation.Async;
import java.io.File;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;

/**
 * 以下接口源于：https://github.com/Deamer1102/ExportExcelData.git
 * @Author liming
 * @Date 2024/8/9 11:06
 */
@Slf4j
@Service
public class AsyncBatchExportExcelServiceImpl implements IAsyncBatchExportExcelService {

    @Resource
    private MyExcelExportUtil myExcelExportUtil;

    // 假定数据量是40万
    public static final long DATA_TOTAL_COUNT = 400000;
    // 查询要导出的批次数据
    static List<Object> list = new ArrayList<>();

    static {
        for (int i = 0; i < DATA_TOTAL_COUNT; i++) {  //模拟库中一百万数据量
            TestExcel testExcel = new TestExcel();
            testExcel.setId("1"+i);
            testExcel.setName("子龙"+i);
            testExcel.setBirthday(new Date());
            testExcel.setPhone("1320000"+i);
            testExcel.setRemark("备注"+i);
            list.add(testExcel);
        }
    }


    /**
     * 1、需求背景:为提升导出数据的性能，采用多线程的方式实现导出百万级别的数据到excel
     * <p>
     * 2、考虑前提
     * 大数据量导出到文件，首先需要考虑的是内存溢出的场景：数据库读取数据到内存中、将数据写入到excel进行大量的IO操作。
     * 考虑到一个文件数据过大，用户打开慢，体验不好。
     * <p>
     * 3、实现思路
     * <p>
     * （1）计算导出数据的总条数：dataTotalCount。
     * （2）合理设置每个excel的数据的数量（避免打开一个excel时间过长）：LIMIT。
     * （3）计算出需要导出的excel个数（线程个数）：count=dataTotalCount/ LIMIT + (dataTotalCount% LIMIT > 0 ? 1 : 0)。
     * （4）将分页、生成文件路径信息，初始化到一个队列里面，队列的大小是线程的数量，对每个文件开启一个线程，异步执行导出，文件全部导出结束，此时异步转成同步，将最终生成的excel文件生成zip压缩包。
     * <p>
     * 4.1、多线程批量导出excel工具类
     */
    @Async("threadPoolTaskExecutor")
    @Override
    public void executeAsyncTask(Map<String,Object> map, CountDownLatch cdl) {
        long start = System.currentTimeMillis();
        int currentPage = (int) map.get("page");
        int pageSize = (int) map.get("limit");
        List subList = new ArrayList(page(list, pageSize, currentPage));
        int count = subList.size();
        System.out.println("线程：" + Thread.currentThread().getName() + " , 读取数据，耗时 ：" + (System.currentTimeMillis() - start) + "ms");
        StringBuilder filePath = new StringBuilder(map.get("path").toString());
        filePath.append("线程").append(Thread.currentThread().getName()).append("-")
                .append("页码").append(map.get("page")).append(".xlsx");
        // 调用导出的文件方法
        Workbook workbook = myExcelExportUtil.getWorkbook("测试数据", "test", TestExcel.class, subList, ExcelType.XSSF);
        File file = new File(filePath.toString());
        MyExcelExportUtil.exportExcel2(workbook, file);
        long end = System.currentTimeMillis();
        System.out.println("线程：" + Thread.currentThread().getName() + " , 导出excel" + map.get("page") + ".xlsx成功 , 导出数据：" + count + " ,耗时 ：" + (end - start) + "ms");
        // 执行完线程数减1
        cdl.countDown();
        System.out.println("剩余任务数  ===========================> " + cdl.getCount());
    }



    public List page(List list, int pageSize, int page) {
        int totalCount = list.size();
        int pagecount = 0;
        int m = totalCount % pageSize;
        if (m > 0) {
            pagecount = totalCount / pageSize + 1;
        } else {
            pagecount = totalCount / pageSize;
        }
        List subList = new ArrayList<>();
        if (pagecount < page) {
            return subList;
        }

        if (m == 0) {
            subList = list.subList((page - 1) * pageSize, pageSize * (page));
        } else {
            if (page == pagecount) {
                subList = list.subList((page - 1) * pageSize, totalCount);
            } else {
                subList = list.subList((page - 1) * pageSize, pageSize * (page));
            }
        }
        return subList;
    }

}
