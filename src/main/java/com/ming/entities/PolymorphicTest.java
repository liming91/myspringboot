package com.ming.entities;

import com.ming.service.IPolymorphicService;

import java.util.List;
import java.util.Map;

/**
 * @Author liming
 * @Date 2024/7/4 16:14
 */
public class PolymorphicTest implements Runnable{
    private List<Map<String, Object>> list;

    private IPolymorphicService iPolymorphicService;

    public PolymorphicTest(List<Map<String, Object>> list, IPolymorphicService iPolymorphicService) {
        this.list = list;
        this.iPolymorphicService = iPolymorphicService;
    }

    @Override
    public void run() {
        List<Map<String, Object>> infoList = iPolymorphicService.infoList();
        list.addAll(infoList);
    }
}
