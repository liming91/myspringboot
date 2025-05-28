package com.ming.util.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * 自第一合并列
 */
public class CustomExcelExportUtil {

    public static Workbook exportExcelWithMerge(ExportParams params, Class<?> pojoClass, List<?> dataList) {
        Workbook workbook = ExcelExportUtil.exportExcel(params, pojoClass, dataList);
        // 插入序号列
        //insertIndexColumn(workbook);
        //insertSubTitleRow(workbook, "2025年5月28日", 16);
        // 手动合并相同内容的列
        mergeSameCells(workbook, 0);
        mergeSameCells(workbook, 1);
        mergeSameCells(workbook, 2);
        return workbook;
    }

    /**
     * 合并指定列中连续相同的单元格
     */
    private static void mergeSameCells(Workbook workbook, int columnIndex) {
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum();

        int startRow = 0;
        for (int i = 1; i <= rowCount; i++) {
            Row currentRow = sheet.getRow(i);
            Row previousRow = sheet.getRow(i - 1);

            if (currentRow == null || previousRow == null) continue;

            Cell currentCell = currentRow.getCell(columnIndex);
            Cell previousCell = previousRow.getCell(columnIndex);

            if (!isCellEqual(currentCell, previousCell)) {
                if (i - startRow > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(startRow, i - 1, columnIndex, columnIndex));
                }
                startRow = i;
            }
        }

        // 处理最后一组
        if (rowCount - startRow >= 1) {
            sheet.addMergedRegion(new CellRangeAddress(startRow, rowCount, columnIndex, columnIndex));
        }
    }


    /**
     * 判断两个单元格是否相等（支持多种类型）
     */
    private static boolean isCellEqual(Cell cell1, Cell cell2) {
        if (cell1 == null || cell2 == null) {
            return false;
        }

        // 处理不同单元格类型
        if (cell1.getCellType() != cell2.getCellType()) {
            return false; // 类型不同，不相等
        }

        switch (cell1.getCellType()) {
            case STRING:
                return cell1.getStringCellValue().equals(cell2.getStringCellValue());

            case NUMERIC:
                // 包括日期和普通数字
                if (DateUtil.isCellDateFormatted(cell1) && DateUtil.isCellDateFormatted(cell2)) {
                    return cell1.getDateCellValue().equals(cell2.getDateCellValue());
                } else {
                    return Double.compare(cell1.getNumericCellValue(), cell2.getNumericCellValue()) == 0;
                }

            case BOOLEAN:
                return cell1.getBooleanCellValue() == cell2.getBooleanCellValue();

            case FORMULA:
                // 如果是公式，比较公式字符串（如果需要比较结果，需额外处理）
                return cell1.getCellFormula().equals(cell2.getCellFormula());

            case BLANK:
                return true; // 都是空白单元格

            default:
                return false;
        }

    }



}
