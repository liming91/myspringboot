package com.ming.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 目前引入mp jar报错 原因实体类映射表报错没有表，不做mp jar引入
 * 注释mp
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@TableName("HB_BASEENTERUSER")
public class HbBaseEnterUser implements Serializable {
    private static final long serialVersionUID = -2395447908171308231L;

    //@TableId("ID")
    private String id;

    //@TableField("ENTERID")
    private String enterId;

    //@TableField("USERID")
    private String userId;

    //@TableField("ADDTIME")
    private Date addTime;

    //@TableField("ADDER")
    private String adder;

    //@TableField(exist = false)
    private List<GrantUser> grantUsers;

}
