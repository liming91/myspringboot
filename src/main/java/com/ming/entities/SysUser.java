package com.ming.entities;


import com.ming.annotation.Desensitized;
import com.ming.enums.SensitiveTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户对象 sys_user
 * 
 */
@Data
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 部门ID */
    private Long deptId;

    /** 用户账号 */
    private String userName;
    private String login_name;


    /** 用户邮箱 */
    @Desensitized(type = SensitiveTypeEnum.EMAIL)
    private String email;

    /** 手机号码 */
    @Desensitized(type = SensitiveTypeEnum.MOBILE_PHONE)
    private String phonenumber;


    /** 用户性别 */
    private String sex;

    /** 用户头像 */
    private String avatar;

    /** 密码 */
    private String password;

    /** 帐号状态（0正常 1停用） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 最后登录IP */
    private String loginIp;

    /** 最后登录时间 */
    private Date loginDate;

    private Long version;

}
