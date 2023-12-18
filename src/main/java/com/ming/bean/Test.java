package com.ming.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ExcelTarget("userExcel")
public class Test implements Serializable {

    @Excel(name = "序号",orderNum = "1")
    private String id;

    @Excel(name = "姓名")
    private String name;

    @Excel(name = "图片")
    private String image;

    private String groupTime;

    private String dateTime;

    private Double dataValue;

}
