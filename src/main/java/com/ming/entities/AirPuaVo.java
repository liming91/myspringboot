package com.ming.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AirPuaVo {

    @ApiModelProperty(value = "设备编码")
    private String equ;

    @ApiModelProperty(value = "温度")
    private String temp;

    @ApiModelProperty(value = "湿度")
    private String hum;

    @ApiModelProperty(value = "状态  0：停止 1：运行")
    private String status;

    @ApiModelProperty(value = "设定温度")
    private String setTemp;

    @ApiModelProperty(value = "设定湿度")
    private String setHum;
}
