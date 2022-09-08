package com.ming.entities;

import lombok.AllArgsConstructor;

/**
 * @param {params}
 * @author hpz
 * @date 2019/10/31 14:45
 * @return ${return}
 */

@AllArgsConstructor
public enum EnergyType {
    electricity,
    water,
    // 医疗气体
    medicalGas,
    //冷热源
    coldHeatSource,
    //蒸汽
    steam,
    //燃气
    gas,


}
