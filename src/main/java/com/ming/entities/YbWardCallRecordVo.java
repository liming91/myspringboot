package com.ming.entities;

import lombok.Data;

/**
 * 南昌修改
 */
@Data
public class YbWardCallRecordVo {
    /**
     * 0代表医标，1代表全时通，2更新护士站大屏布局, 3空调 4、医气，5慈安通推送
     */
    private String code;


    private AirPuaVo airPuaVo;


}
