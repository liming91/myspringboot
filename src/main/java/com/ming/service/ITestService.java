package com.ming.service;

import com.ming.bean.Test;

import java.util.List;
import java.util.Map;

public interface ITestService {

    Map<String, Object> getList(int dateType);

    Map<String, Object> list(int dateType);

    List<Map<String, Object>> listTime(int dateType);
}
