package com.ming.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @TableName info
 */
@TableName(value = "info")
@Data
public class Info implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField(value = "name")
    private String name;

    /**
     *
     */
    @TableField(value = "tel")
    private String tel;

    /**
     *
     */
    @TableField(value = "time")
    private Date time;


    @TableField(value = "del")
    private Integer del;

    @ApiModelProperty("删除时间")
    @TableField(value = "del")
    private Date del_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}