package com.ming.service;

import com.ming.bean.Test;
import com.ming.entities.VO.WeekData;

import java.util.List;
import java.util.Map;

public interface ITestService {

    Map<String, Object> getList(int dateType);

    Map<String, Object> list(int dateType);

    List<Map<String, Object>> listTime(int dateType,String startTime, String endTime);

    List<Map<String, Object>> fenTime(int dateType, String startTime, String endTime);

    List<WeekData> week();

    Map<String, Object> findSevenDate();

    List<Test> getList();
}
