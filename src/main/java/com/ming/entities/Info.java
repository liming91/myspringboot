package com.ming.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.ming.annotation.DecryptField;
import com.ming.annotation.EncryptField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

    @NotEmpty(message="不能为空")
    @TableField(value = "name")
    private String name;

    /**
     *
     */
    @EncryptField
    @DecryptField
    @TableField(value = "tel")
    private String tel;

    /**
     *
     */
    @TableField(value = "time")
    private Date time;


    @TableField(value = "del_fag")
    private Integer del_fag;

    @ApiModelProperty("删除时间")
    @TableField(value = "del_time")
    private Long del_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}