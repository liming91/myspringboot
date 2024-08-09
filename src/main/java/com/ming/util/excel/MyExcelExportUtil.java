package com.ming.util.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.ming.util.ExcelStyleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel导出工具类
 * @Author liming
 * @Date 2024/8/9 14:46
 */
@Slf4j
@Component
public class MyExcelExportUtil {

//    @Resource
//    private IExcelExportServer excelExportServer;

    /**
     * 小量数据允许导出的最大条数
     */
    private static final Integer EXPORT_EXCEL_BASE_MAX_NUM = 100000;
    public static int USE_SXSSF_LIMIT = 100000;

    /**
     * 获取导出的 Workbook对象
     * 普通导出
     *
     * @param title     大标题
     * @param sheetName 页签名
     * @param object    导出实体
     * @param list      普通导出传入的数据集合
     * @param list      数据集合
     * @return Workbook
     */
    public static Workbook getWorkbook(String title, String sheetName, Class<?> object, List<?> list, ExcelType excelType) {
        // 判断导出数据是否为空
        if (list == null) {
            list = new ArrayList<>();
        }
        // 判断导出数据数量是否超过限定值
//        if (list.size() > EXPORT_EXCEL_BASE_MAX_NUM) {
//            title = "导出数据行数超过:" + EXPORT_EXCEL_BASE_MAX_NUM + "条，无法导出！";
//            list = new ArrayList<>();
//        }
        // 获取导出参数
        ExportParams exportParams = new ExportParams(title, sheetName, excelType);
        // 设置导出样式
        exportParams.setStyle(ExcelStyleUtil.class);
        // 设置行高
        exportParams.setHeight((short) 8);
        // 普通导出，输出Workbook流
       return ExcelExportUtil.exportExcel(exportParams, object, list);
        //return createExcel(exportParams, object, list);
    }

    public static void exportExcel2(Workbook workbook, File file) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            // 输出表格
            workbook.write(out);
        } catch (IOException e) {
            log.error("文件导出异常，详情如下:", e);
            throw new RuntimeException("文件导出异常");
        } finally {
            try {
                if (workbook != null) {
                    // 关闭输出流
                    workbook.close();
                }
                if (out != null) {
                    // 关闭输出流
                    out.close();
                }
            } catch (IOException e) {
                log.error("文件导出异常,详情如下:", e);
            }
        }
    }

}
