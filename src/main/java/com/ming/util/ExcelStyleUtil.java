package com.ming.util;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.params.ExcelForEachParams;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

/**
 * excel样式
 *
 * @Author liming
 * @Date 2023/7/18 15:37
 */

public class ExcelStyleUtil implements IExcelExportStyler {
    // 数据行类型
    public static final String DATA_STYLES = "dataStyles";
    // 标题类型
    public static final String TITLE_STYLES = "titleStyles";

    // 标题类型
    public static final String HEADER_STYLES = "headerStyles";

    // 备注类型
    public static final String REMARK_STYLES = "remarkStyles";

    //数据行样式
    private CellStyle styles;
    // 标题样式
    private CellStyle titleStyle;

    private CellStyle headerStyle;

    public ExcelStyleUtil(Workbook workbook) {
        this.init(workbook);
    }

    private void init(Workbook workbook) {
        this.styles = initStyles(workbook);
        this.titleStyle = initTitleStyle(workbook);
        this.headerStyle = initHeaderStyle(workbook);
    }


    @Override
    public CellStyle getHeaderStyle(short i) {
        return headerStyle;
    }

    /**
     * 标题样式
     */
    @Override
    public CellStyle getTitleStyle(short i) {
        return titleStyle;
    }

    @Override
    public CellStyle getStyles(boolean parity, ExcelExportEntity entity) {
        return styles;
    }

    /**
     * 获取样式方法
     *
     * @param dataRow 数据行
     * @param obj     对象
     * @param data    数据
     */
    @Override
    public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data) {
        return getStyles(true, entity);
    }

    @Override
    public CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams) {
        return null;
    }


    /**
     * 初始化--标题行样式
     *
     * @param workbook
     * @return
     */
    private CellStyle initTitleStyle(Workbook workbook) {
        return buildCellStyle(workbook, TITLE_STYLES);
    }

    /**
     * 初始化--数据行样式
     *
     * @param workbook
     * @return
     */
    private CellStyle initStyles(Workbook workbook) {
        return buildCellStyle(workbook, DATA_STYLES);
    }

    private CellStyle initHeaderStyle(Workbook workbook) {
        return buildCellStyle(workbook, HEADER_STYLES);
    }

    /**
     * 设置单元格样式
     *
     * @param workbook
     * @param type     类型  用来区分是数据行样式还是标题样式
     * @return
     */
    public CellStyle buildCellStyle(Workbook workbook, String type) {
        CellStyle style = workbook.createCellStyle();
        // 字体样式
        Font font = workbook.createFont();
        if (TITLE_STYLES.equals(type)) {
            font.setFontHeightInPoints((short) 12);
            font.setBold(true);
            font.setFontName("Courier New");
            style.setFont(font);

            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        if (DATA_STYLES.equals(type)) {
            font.setFontHeightInPoints((short) 10);
            font.setFontName("Courier New");
            style.setFont(font);
        }

        if (HEADER_STYLES.equals(type)) {
            font.setFontHeightInPoints((short) 12);
            font.setBold(true);
            font.setFontName("Courier New");
            style.setFont(font);

            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        if (REMARK_STYLES.equals(type)) {
            font.setFontHeightInPoints((short) 12);
            // 背景色
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        font.setFontName("Courier New");
        style.setFont(font);
        // 设置底边框
        style.setBorderBottom(BorderStyle.THIN);
        // 设置左边框
        style.setBorderLeft(BorderStyle.THIN);
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 设置底边颜色
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 字体样式
     *
     * @param size   字体大小
     * @param isBold 是否加粗
     * @return
     */
    private Font getFont(Workbook workbook, short size, boolean isBold) {
        Font font = workbook.createFont();
        // 字体样式
        font.setFontName("楷体");
        // 是否加粗
        font.setBold(isBold);
        // 字体大小
        font.setFontHeightInPoints(size);
        return font;
    }


}
