package com.ming.service.impl;

import com.ming.bean.Test;
import com.ming.entities.DataTrendVO;
import com.ming.mapper.TestMapper;
import com.ming.service.ITestService;
import com.ming.util.DateUtil;
import com.ming.util.FullDateHandle;
import com.ming.util.QuarterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements ITestService {
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TestMapper testMapper;

    @Override
    public Map<String, Object> getList(int dateType) {
        Map<String, Object> resMap = new HashMap<>();
        String startTime = DateUtil.getNowDate("yyyy-MM-dd");
        String endTime = DateUtil.getNowDate("yyyy-MM-dd");
        List<Test> list = testMapper.select();
        List<DataTrendVO> dataList = new ArrayList<>();
        //日期类型 0:日 1:月 2:年 3:季
        list.stream().forEach(x -> {
            DataTrendVO dataTrendVO = new DataTrendVO();
           String time = "2021-12-02 15:04:59";
            String subTime = null;
            if (dateType == 0) {
                subTime = time.substring(11, 13);
                log.info("subTime:0:{}", subTime);

            }
            if (dateType == 1) {
                subTime = time.substring(5, 10);
                log.info("subTime:1:{}", subTime);

            }
            if (dateType == 2) {
                subTime =time.substring(0, 7);
                log.info("subTime:2:{}", subTime);
            }
            dataTrendVO.setDateTime(subTime);
            dataList.add(dataTrendVO);
        });
        //日期类型 0：日 1:月  2:年
        //dateType前端传参 日期类型 0：日 1:月  2:年 3:季
        if (dateType == 0) {
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
        }
        if (dateType == 1) {
            startTime = DateUtil.getCurrMonthFistDay();
            endTime = DateUtil.getCurrMonthLastDay();
        }
        if (dateType == 2) {
            DateTimeFormatter dailyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Year years = Year.now();
            String start = years.atDay(1).format(dailyFormatter);
            String end = years.atMonth(12).atEndOfMonth().format(dailyFormatter);
            startTime = start;
            endTime = end;
        }
        //查询本季季度
        Date date = QuarterUtils.parseDate(startTime);
        String oneQuarter = QuarterUtils.formatDate(QuarterUtils.getSeasonDate(date)[0], "yyyy-MM");
        String twoQuarter = QuarterUtils.formatDate(QuarterUtils.getSeasonDate(date)[1], "yyyy-MM");
        String threeQuarter = QuarterUtils.formatDate(QuarterUtils.getSeasonDate(date)[2], "yyyy-MM");
        if (dateType == 3) {
            startTime = oneQuarter + "-01";
            endTime = threeQuarter + "-31";
        }
        if (dateType == 0 || dateType == 1 || dateType == 2) {
            log.info("dateType:{}", dateType);
            log.info("startTime:{}", startTime);
            // 补全查询出来的日期数据
            resMap.put("dateList", FullDateHandle.bimDataHandle(dataList, dateType, startTime));
        }
        //季度处理
        if (dateType == 3) {
            //数据源
            List<DataTrendVO> proList = new ArrayList<>();
            //构造的日期
            List<String> dateList = new ArrayList<>();
            dateList.add(oneQuarter);
            dateList.add(twoQuarter);
            dateList.add(threeQuarter);
            List<DataTrendVO> proQuarterList = new ArrayList<>();
            dateList.stream().forEach(x -> {
                if (proList != null && proList.size() > 0) {
                    proList.stream().forEach(p -> {
                        DataTrendVO dataTrendVO = new DataTrendVO();
                        if (p.getDateTime().equals(x)) {
                            dataTrendVO.setDateTime(p.getDateTime());
                            dataTrendVO.setDataValue(p.getDataValue());
                        } else {
                            dataTrendVO.setDateTime(x);
                            dataTrendVO.setDataValue((double) 0);
                        }
                        proQuarterList.add(dataTrendVO);
                    });
                }
            });

            List<DataTrendVO> proCollect = proQuarterList.stream().collect(Collectors.toMap(DataTrendVO::getDateTime, a -> a, (o1, o2) -> {
                o1.setDataValue(o1.getDataValue() + o2.getDataValue());
                return o1;
            })).values().stream().collect(Collectors.toList());
            proCollect.sort(Comparator.comparing(m -> (String) m.getDateTime()));
        }
        return resMap;
    }
}
