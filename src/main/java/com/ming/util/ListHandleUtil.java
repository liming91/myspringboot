package com.ming.util;

import com.google.common.collect.Maps;
import com.ming.entities.VO.DataTrendListVo;
import com.ming.entities.VO.DataTrendVO;


import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description: ListHandleUtil
 * <p>
 * date: 2021/7/27 15:31
 * author: zsq
 * version: 1.0
 */
public class ListHandleUtil {

    /**
     * @description: 合并list并将相同字段求值
     *
     * @author: zsq
     * @date: 2021/7/27 17:43
     * @param: list
     * @return: java.util.List<com.yb.code.entity.vo.DataTrendVO>
     */
    public static List<DataTrendVO> merge(List<DataTrendVO> list) {
        List<DataTrendVO> result = list.stream()
                // 表示dateTime为key， 接着如果有重复的，那么从DataTrendVO对象o1与o2中筛选出一个，这里选择o1，
                // 并把dateTime重复，需要将nums和sums与o1进行合并的o2, 赋值给o1，最后返回o1
                .collect(Collectors.toMap(DataTrendVO::getDateTime, a -> a, (o1, o2)-> {
                    o1.setDataValue(MathUtils.formatDigit(o1.getDataValue() + o2.getDataValue(), 2));
                    return o1;
                })).values().stream().collect(Collectors.toList());
        result = result.stream().sorted(Comparator.comparing(DataTrendVO::getDateTime)).collect(Collectors.toList());
        return result ;
    }

    /**
     * @description: 合并list并将相同字段求值
     *
     * @author: zsq
     * @date: 2021/7/27 17:43
     * @param: list
     * @return: java.util.List<com.yb.code.entity.vo.DataTrendVO>
     */
    public static Map<String, List<DataTrendVO>> merge(List<DataTrendListVo> list, Integer dateType) {
        // 处理结果
        Map<String, List<DataTrendVO>> resMap = Maps.newHashMap();
        // 根据日期分组
        Map<String, List<DataTrendListVo>> mapList = list.stream().collect(Collectors.groupingBy(DataTrendListVo::getGroupTime));
        //根据名字合并
//        Map<String, List<EnergyReportDTO>> map = waterCountEnergyList.stream().collect(Collectors.groupingBy(EnergyReportDTO::getBuildName));
//        for (Map.Entry<String, List<EnergyReportDTO>> entry : map.entrySet()) {
//            List<EnergyReportDTO> value = entry.getValue();
//            value.stream().collect(Collectors.groupingBy(item -> (item.getDepartId() + item.getDepartName()), Collectors.toList())).forEach(
//                    (id, transfer) -> {
//                        transfer.stream().reduce((a, b) -> new EnergyReportDTO(a.getGardenName(), a.getBuildName(), a.getFloorName(),
//                                a.getDepartId(), a.getDepartName(), a.getAddr(),
//                                getFormatDouble(a.getEnergy() + b.getEnergy()), getFormatDouble(a.getCost() + b.getCost()), a.getSortNum())).ifPresent(newWaterCountEnergyList::add);
//                    }
//            );
//        }
        // 数据求和处理
        mapList.forEach((k, v) -> {
            List<DataTrendVO> timeList = v.stream()
                    // 表示dateTime为key， 接着如果有重复的，那么从DataTrendVO对象o1与o2中筛选出一个，这里选择o1，
                    // 并把dateTime重复，需要将nums和sums与o1进行合并的o2, 赋值给o1，最后返回o1
                    .collect(Collectors.toMap(DataTrendListVo::getDateTime, a -> a, (o1, o2) -> {
                        o1.setDataValue(MathUtils.formatDigit(o1.getDataValue() + o2.getDataValue(), 2));
                        return o1;
                    })).values().stream().map(d -> {
                        DataTrendVO dataTrendVO = new DataTrendVO();
                        dataTrendVO.setDateTime(d.getDateTime());
                        dataTrendVO.setDataValue(d.getDataValue());
                        return dataTrendVO;
                    }).collect(Collectors.toList());
            // 根据日期排序
            timeList = timeList.stream().sorted(Comparator.comparing(DataTrendVO::getDateTime)).collect(Collectors.toList());
            // 数据补全
            timeList = FullDateHandle.dataHandle(timeList, dateType);
            resMap.put(k, timeList);
        });
        return resMap ;
    }

}
