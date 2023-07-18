package com.ming.entities.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cdd
 * @Date 2020/10/22 18:07
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EleWaterVO {

    /**
     * 回路名称
     */
    private String eleComeBackName;
    /**
     * 地址
     */
    private String eleAddress;
    /**
     * 读数
     */
    private Double eleEnergy;
    /**
     * 费用
     */
    private Double eleCost;




    /**
     * 回路名称
     */
    private String waterComeBackName;
    /**
     * 地址
     */
    private String waterAddress;
    /**
     * 读数
     */
    private Double waterEnergy;
    /**
     * 费用
     */
    private Double waterCost;

    public EleWaterVO(String waterComeBackName, String waterComeBackName1) {
        this.waterComeBackName =waterComeBackName;
        this.eleComeBackName =waterComeBackName1;
    }
}
