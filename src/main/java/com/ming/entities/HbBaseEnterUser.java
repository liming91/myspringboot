package com.ming.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("HB_BASEENTERUSER")
public class HbBaseEnterUser implements Serializable {
    private static final long serialVersionUID = -2395447908171308231L;

    @TableId("ID")
    private String id;

    @TableField("ENTERID")
    private String enterId;

    @TableField("USERID")
    private String userId;

    @TableField("ADDTIME")
    private Date addTime;

    @TableField("ADDER")
    private String adder;

    @TableField(exist = false)
    private List<GrantUser> grantUsers;

}
