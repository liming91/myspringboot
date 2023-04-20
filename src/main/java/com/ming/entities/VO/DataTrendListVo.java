package com.ming.entities.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class DataTrendListVo {

    @ApiModelProperty("时间点")
    private String dateTime;

    @ApiModelProperty("数值")
    private Double dataValue;

    @ApiModelProperty("时间分组")
    private String groupTime;
}
