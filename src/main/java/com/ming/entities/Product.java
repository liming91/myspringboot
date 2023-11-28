package com.ming.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * @TableName product
 */
@TableName(value = "product")
@Data
public class Product implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "ID", type = IdType.INPUT)
    private String id;

    /**
     *
     */
    @TableField("name")
    private String name;

    /**
     *
     */
    @TableField("num")
    private Integer num;

    /**
     *
     */
    @TableField("version")
    private Integer version;


}