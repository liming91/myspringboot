package com.ming.entities;

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
public class TestExcel {

    @TableId(value = "id")
    private String id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "birthday")
    private Date birthday;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "remark")
    private String remark;
}
