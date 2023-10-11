package com.ming.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName info
 */
@TableName(value ="info")
@Data
public class Info implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private String id;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}