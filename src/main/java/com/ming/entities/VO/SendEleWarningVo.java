package com.ming.entities.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SendEleWarningVo {

    @ApiModelProperty(value = "企业编码")
    private String enterCode;

    @ApiModelProperty(value = "预警级别")
    private String levels;

    @ApiModelProperty(value = "区域名称")
    private String areaName;


    @ApiModelProperty(value = "企业名称")
    private String enterName;


    @ApiModelProperty(value = "报警类型(2:治污停用 11：应急报警)")
    private String wtype;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "报警时长")
    private String timeStr;

    @ApiModelProperty(value = "预警状态(2:已处理 3:未处理)")
    private String status;

    @ApiModelProperty(value = "报警内容")
    private String content;




}
