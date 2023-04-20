package com.ming.entities.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataTrendVO {
    // 时间点
    private String dateTime;


    // 数值
    private Double dataValue;

}
