package com.ming.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ming.bean.Test;
import com.ming.entities.CalendarUtil;
import com.ming.entities.VO.DataTrendListVo;
import com.ming.entities.VO.DataTrendVO;
import com.ming.entities.VO.WeekData;
import com.ming.enums.DateTypeEnum;
import com.ming.mapper.TestMapper;
import com.ming.service.ITestService;
import com.ming.util.DateUtils;
import com.ming.util.FullDateHandle;
import com.ming.util.QuarterUtils;
import com.ming.util.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
        List<Test> list = testMapper.getDateByTime(DateTypeEnum.getQueryDateFormatterEnum(dateType), DateTypeEnum.getResDateFormatterEnum(dateType),
                DateTypeEnum.getDateByType(dateType), DateTypeEnum.getDateByType(dateType));
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
        List<String> times = list.stream().map(Test::getDateTime).collect(Collectors.toList());
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
        list.sort(Comparator.comparing(Test::getDateTime));
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

        List<Test> list = testMapper.getDateByTime(DateTypeEnum.getQueryDateFormatterEnum(dateType), DateTypeEnum.getResDateFormatterEnum(dateType),
                startTime, endTime);
        Map<String, List<Test>> dataMap = list.stream().collect(Collectors.groupingBy(Test::getGroupTime));
        Map<String, List<DataTrendVO>> resMap = Maps.newHashMap();
        for (Map.Entry<String, List<Test>> entry : dataMap.entrySet()) {
            List<Test> value = entry.getValue();
            List<DataTrendVO> dataTrendVOList = new ArrayList<>();
            value.forEach(v -> {
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
     *
     * @param map
     * @return
     */
    public static Map<String, List<DataTrendVO>> mapSortByKey(Map<String, List<DataTrendVO>> map) {
        Map<String, List<DataTrendVO>> result = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
        return result;
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


    @Override
    public List<WeekData> week() {
        List<Test> list = testMapper.select();
        List<WeekData> respList = new ArrayList<>();
        Map<String, List<Test>> collect = list.stream().collect(Collectors.groupingBy(Test::getName));
        for (Map.Entry<String, List<Test>> entry : collect.entrySet()) {
            WeekData weekData = new WeekData();
            weekData.setName(entry.getKey());
            weekData.setData(getWeekCount(entry.getValue()));
            respList.add(weekData);
        }
        return respList;
    }

    /**
     * 按照周统计数量
     *
     * @param testList
     * @return
     */
    public JSONObject getWeekCount(List<Test> testList) {
        // 获取本周一和本周日的日期
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        LocalDate sunday = today.with(DayOfWeek.SUNDAY);

        // 初始化每天的计数为0
        int[] countPerDay = new int[7];

        // 遍历列表中的每个事件
        for (Test test : testList) {
            DateTime dateTime1 = DateUtil.parse(test.getDateTime(), DatePattern.NORM_DATE_PATTERN);
            LocalDate dateTime = LocalDate.parse(DateUtil.format(dateTime1, DatePattern.NORM_DATE_PATTERN));
            // 如果事件日期在本周一到周日的范围内，则计数加1
            if (!dateTime.isBefore(monday) && !dateTime.isAfter(sunday)) {
                int dayOfWeek = dateTime.getDayOfWeek().getValue();
                countPerDay[dayOfWeek - 1]++;
            }
        }

        JSONObject jsonObject = new JSONObject();
        // 输出每天的数量
        for (int i = 0; i < 7; i++) {
            DayOfWeek dayOfWeek = DayOfWeek.of(i + 1);
            jsonObject.put(String.valueOf(dayOfWeek.getValue()), countPerDay[i]);
        }
        return jsonObject;
    }


    @Override
    public Map<String, Object> findSevenDate() {
        Map<String, Object> resMap = new HashMap<>(2);
        List<String> sevenDate = getSevenDate(7);
        List<DataTrendVO> dataTrendVOList = testMapper.findSevenDate();
        List<DataTrendVO> dataList = dataList(sevenDate, dataTrendVOList);
        resMap.put("rainFall", dataList);
        return resMap;
    }

    /**
     * 对最近7天的数据进行处理
     *
     * @param sevenDate
     * @param dataList
     * @return
     */
    private static List<DataTrendVO> dataList(List<String> sevenDate, List<DataTrendVO> dataList) {
        List<DataTrendVO> resList = new ArrayList<>();
        for (String s : sevenDate) {
            DataTrendVO dataTrendVO = new DataTrendVO();
            dataTrendVO.setDateTime(s);
            boolean b = false;
            for (DataTrendVO data : dataList) {
                if (s.equals(data.getDateTime())) {
                    dataTrendVO.setDataValue(data.getDataValue());
                    resList.add(dataTrendVO);
                    b = true;
                    break;
                }
            }
            if (!b) {
                dataTrendVO.setDataValue(0.0);
                resList.add(dataTrendVO);
            }
        }
        return resList;
    }

    /**
     * 最近7天数据
     *
     * @param day
     * @return
     */
    public static List<String> getSevenDate(int day) {
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < day; i++) {
            Date date = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), -i);
            dateList.add(DateUtil.format(date, "MM-dd"));
        }
        Collections.sort(dateList);
        return dateList;
    }


    /**
     * 12月数据
     * @return
     */
    public static List<String> getYearStr() {
        List<String> dateList = CalendarUtil.queryData("2024-01-01", "2024-12-31", 2);
        Collections.sort(dateList);
        return dateList;
    }

    /**
     * 最近12月数据
     * @return
     */
    public static List<String> getYearStr2() {
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Date date = org.apache.commons.lang3.time.DateUtils.addMonths(new Date(), i);
            dateList.add(DateUtil.format(date, "yyyy-MM"));
        }
        Collections.sort(dateList);
        return dateList;
    }


    @Override
    public List<Test> getList() {
        return testMapper.select();
    }
}
