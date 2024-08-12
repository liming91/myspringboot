package com.ming.entities;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @Author liming
 * @Date 2024/8/9 14:21
 */
@Data
@TableName(value = "test_excel")
@ExcelTarget("testExcel")
public class TestExcel {

    @Excel(name = "序号")
    @TableId(value = "id")
    private String id;

    @Excel(name = "名称")
    @TableField(value = "name")
    private String name;

    @Excel(name = "生日")
    @TableField(value = "birthday")
    private Date birthday;

    @Excel(name = "手机号")
    @TableField(value = "phone")
    private String phone;

    @Excel(name = "备注")
    @TableField(value = "remark")
    private String remark;
}
