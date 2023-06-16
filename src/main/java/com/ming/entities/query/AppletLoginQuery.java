package com.ming.entities.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xieyuxing
 */
@Data
@ApiModel("小程序登录信息")
public class AppletLoginQuery {

    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    private String userJobNo;


    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String pwd;

    /**
     * 微信code
     */
    @ApiModelProperty("微信code")
    private String code;

    /**
     * 微信code类型
     */
    @ApiModelProperty("微信code类型")
    private String codeType;
}
