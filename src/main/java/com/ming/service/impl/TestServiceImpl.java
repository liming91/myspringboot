package com.ming.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ming.bean.Test;
import com.ming.entities.CalendarUtil;
import com.ming.entities.VO.DataTrendListVo;
import com.ming.entities.VO.DataTrendVO;
import com.ming.enums.DateTypeEnum;
import com.ming.mapper.TestMapper;
import com.ming.service.ITestService;
import com.ming.util.DateUtils;
import com.ming.util.FullDateHandle;
import com.ming.util.QuarterUtils;
import org.checkerframework.checker.units.qual.A;
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
        String startTime = DateUtils.getNowDate("yyyy-MM-dd");
        String endTime = DateUtils.getNowDate("yyyy-MM-dd");
        List<Test> list = testMapper.getDateByTime(DateTypeEnum.getQueryDateFormatterEnum(dateType), DateTypeEnum.getResDateFormatterEnum(dateType), DateTypeEnum.getDateByType(dateType));
        List<DataTrendVO> dataList = new ArrayList<>();
        list.forEach(x -> {
            DataTrendVO dataTrendVO = new DataTrendVO();
            dataTrendVO.setDateTime(x.getDateTime());
            dataTrendVO.setDataValue(x.getDataValue());
            dataList.add(dataTrendVO);
        });

        //日期类型 0：日 1:月  2:年
        // dateType前端传参 日期类型 0：日 1:月  2:年 3:季
        if (dateType == 0) {
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
        }
        if (dateType == 1) {
            startTime = DateUtils.getCurrMonthFistDay();
            endTime = DateUtils.getCurrMonthLastDay();
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
            //如果日期相同对应的值求和处理
            List<DataTrendVO> proCollect = proQuarterList.stream().collect(Collectors.toMap(DataTrendVO::getDateTime, a -> a, (o1, o2) -> {
                o1.setDataValue(o1.getDataValue() + o2.getDataValue());
                return o1;
            })).values().stream().collect(Collectors.toList());
            proCollect.sort(Comparator.comparing(m -> (String) m.getDateTime()));
        }
        return resMap;
    }

    @Override
    public Map<String, Object> list(int dateType) {
        Map<String, Object> resMap = new Hashtable<>();
        List<Test> list = testMapper.getList(DateTypeEnum.getQueryDateFormatterEnum(dateType), DateTypeEnum.getResDateFormatterEnum(dateType), DateTypeEnum.getDateByType(dateType));
        //查询两个日期之间的时间
        List<String> timeList = CalendarUtil.getTimeList(DateUtil.formatDateTime(DateUtil.beginOfMonth(new Date())), DateUtil.formatDateTime(DateUtil.endOfMonth(new Date())), dateType);
        if (dateType == 2) {
            timeList = CalendarUtil.getTimeList(DateUtil.formatDateTime(DateUtil.beginOfYear(new Date())), DateUtil.formatDateTime(DateUtil.endOfYear(new Date())), dateType);
        }
        //已存在的时间
        List<String> times = list.stream().map(m -> m.getDateTime()).collect(Collectors.toList());
        List<String> finalTimeList = new ArrayList<>(timeList);
        timeList.removeAll(times);
        //时间补全
        timeList.forEach(hour -> {
            Test vo = new Test();
            vo.setDateTime(hour);
            vo.setDataValue(0.0);
            list.add(vo);
        });
        List<Test> collect1 = list.stream().filter(f -> !finalTimeList.contains(f.getDateTime())).collect(Collectors.toList());
        list.removeAll(collect1);
        list.sort(Comparator.comparing(m -> m.getDateTime()));
        resMap.put("data", list);
        return resMap;
    }


    @Override
    public List<Map<String, Object>> listTime(int dateType, String startTime, String endTime) {
        List<Map<String, Object>> resList = new ArrayList<>();
        // 日期格式处理
        List<String> timeList = Lists.newArrayList();
        try {
            timeList = CalendarUtil.queryDataDayString(startTime, endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Test> list = testMapper.getDateByTime(DateTypeEnum.getQueryDateFormatterEnum(dateType), DateTypeEnum.getResDateFormatterEnum(dateType), DateTypeEnum.getDateByType(dateType));
        Map<String, List<Test>> dataMap = list.stream().collect(Collectors.groupingBy(Test::getGroupTime));
        Map<String, List<DataTrendVO>> resMap = Maps.newHashMap();
        for (Map.Entry<String, List<Test>> entry : dataMap.entrySet()) {
            List<Test> value = entry.getValue();
            List<DataTrendVO> dataTrendVOList = new ArrayList<>();
            value.forEach(v->{
                DataTrendVO dataTrendVO = new DataTrendVO();
                dataTrendVO.setDateTime(v.getDateTime());
                dataTrendVO.setDataValue(v.getDataValue());
                dataTrendVOList.add(dataTrendVO);
            });
            resMap.put(entry.getKey(), FullDateHandle.dataHandle(dataTrendVOList, 3));
        }
        //已存在的时间数据
        Set<String> collect = list.stream().map(Test::getGroupTime).collect(Collectors.toSet());

        //将不存在的时间补全
        List<String> noExistTimeList = timeList.stream().filter(t -> !collect.contains(t)).collect(Collectors.toList());
        noExistTimeList.stream().forEach(s -> {
            List<DataTrendVO> lists = FullDateHandle.dataHandle(new ArrayList<>(), 3);
            resMap.put(s, lists);
        });
        Map<String, List<DataTrendVO>> resultMap = mapSortByKey(resMap);
        resultMap.forEach((k, v) -> resList.add(ImmutableMap.of("queryData", k, "resList", v)));
        return resList;
    }

    /**
     * map根据key排序
     * @param map
     * @return
     */
    public  static  Map<String, List<DataTrendVO>> mapSortByKey(Map<String, List<DataTrendVO>> map){
        Map<String, List<DataTrendVO>> result = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x->result.put(x.getKey(),x.getValue()));
        return  result;
    }


    @Override
    public List<Map<String, Object>> fenTime(int dateType, String startTime, String endTime) {
        List<Map<String, Object>> resList = new ArrayList<>();
        Map<String, Object> resMap = new HashMap<>();
        List<Test> dataList = testMapper.getList(DateTypeEnum.getQueryDateFormatterEnum(dateType), DateTypeEnum.getResDateFormatterEnum(dateType), DateTypeEnum.getDateByType(dateType));
        List<String> timeList = CalendarUtil.getTimeList(startTime, endTime, dateType);
        List<String> times = dataList.stream().map(m -> m.getDateTime()).collect(Collectors.toList());
        List<String> finalTimeList = new ArrayList<>(timeList);
        timeList.removeAll(times);
        //时间补全
        timeList.forEach(hour -> {
            Test vo = new Test();
            vo.setDateTime(hour);
            vo.setDataValue(0.0);
            dataList.add(vo);
        });
        List<Test> collect1 = dataList.stream().filter(f -> !finalTimeList.contains(f.getDateTime())).collect(Collectors.toList());
        dataList.removeAll(collect1);
        dataList.sort(Comparator.comparing(m -> m.getDateTime()));
        resMap.put("data", dataList);
        resList.add(resMap);
        return resList;
    }


}
