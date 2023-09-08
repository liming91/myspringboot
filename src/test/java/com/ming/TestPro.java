package com.ming;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ming.bean.Order;
import com.ming.bean.Person;
import com.ming.bean.User;
import com.ming.entities.*;
import com.ming.entities.VO.DataTrendVO;
import com.ming.entities.VO.EleWaterVO;
import com.ming.util.PercentUtil;
import com.ming.util.StringUtils;
import org.assertj.core.util.Lists;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;


public class TestPro {

    @Test
    public void lineTest44() throws ExecutionException, InterruptedException {

        DataTrendVO d1 = new DataTrendVO("1", 10.1);
        DataTrendVO d2 = new DataTrendVO("2", 20.1);
        List<DataTrendVO> list = new ArrayList<>();
        list.add(d1);
        list.add(d2);
        Double reduce = list.stream().map(DataTrendVO::getDataValue).reduce(0.00, (a, b) -> a + b);

        System.out.println(reduce);

        Double aDouble = list.stream().map(DataTrendVO::getDataValue).reduce(Double::sum).get();
        System.out.println(aDouble);
//        Double fm = 0.00;
//        fm=122.18*0.56;
//        System.out.println(fm);
//        Double aDouble = billingFormula(34.04, EnergyType.electricity);
//        System.out.println(aDouble);
//        Double ac = PercentUtil.SC(33.04, 10.23);
//        System.out.println(ac);
    }


    public static Double billingFormula(double number, EnergyType decimalPlace) {


        Double fm = 0.00;
        switch (decimalPlace) {
            case water:
                //  水
                fm = number * 5.8;
                break;
            case electricity:
                // 电
                fm = number * 0.56;
                break;
            case gas:
                // 燃气
                fm = number * 2.18;
            default:
                break;
        }

        return fm;

    }


    @Test
    public void DishTest() {
        List<Dish> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Dish dish = new Dish();
            if (i % 2 == 0) {
                dish.setName("照明用电" + i);
                dish.setVegetarian(true);
                dish.setCalories(i);
                dish.setType(new Type(Integer.toString(i), "电"));
            } else {

                dish.setName("水井" + i);
                dish.setVegetarian(true);
                dish.setCalories(i);
                dish.setType(new Type(Integer.toString(i), "水"));
            }
            list.add(dish);
        }

        //System.out.println(JSONArray.parseArray(JSON.toJSONString(list)));
        List<Dish> lowCaloricDishes = new ArrayList<>();
        //1.电量大于2
        for (Dish dish : list) {
            if (dish.getCalories() > 2) {
                lowCaloricDishes.add(dish);
            }
        }
        System.out.println("过滤：" + lowCaloricDishes);
        //2.对于电量大于2的排序
        Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return Integer.compare(o2.getCalories(), o1.getCalories());
            }
        });
        System.out.println("排序：" + lowCaloricDishes);

        //3.获取排序后的名字
        List<String> stringList = new ArrayList<>();
        for (Dish lowCaloricDish : lowCaloricDishes) {
            stringList.add(lowCaloricDish.getName());
        }
        //4.按照种类分类
        System.out.println("排序后的名称：" + stringList);
        Map<Type, List<Dish>> map = new HashMap<>();
        int count = 0;
        for (Dish lowCaloricDish : lowCaloricDishes) {
            if (map.get(lowCaloricDish.getType()) == null) {
                List<Dish> dishList = new ArrayList<>();
                int calories = lowCaloricDish.getCalories();
                count += calories;
                lowCaloricDish.setSum(count);
                dishList.add(lowCaloricDish);
                map.put(lowCaloricDish.getType(), dishList);
            } else {
                map.get(lowCaloricDish.getType()).add(lowCaloricDish);
            }
        }
        System.out.println("分类：" + map);

        //5.按类型分类计算小计和合计
        List<Dish> resultList = Lists.newArrayList();
        Map<String, List<Dish>> collect = list.stream().collect(Collectors.groupingBy(x -> x.getType().getName(), TreeMap::new, Collectors.toList()));
        collect.forEach((k, v) -> {
            Map<String, List<Dish>> listMap = v.stream().collect(Collectors.groupingBy(x -> x.getType().getName()));
            listMap.forEach((x, y) -> {
                int calories = 0;
                if (k.equals(x)) {
                    calories = y.stream().map(Dish::getCalories).reduce(0, (a, b) -> a + b);
                    Dish dish = new Dish();
                    dish.setName("小计");
                    dish.setCalories(calories);
                    collect.get(x).add(dish);
                }
            });
        });
        //再总计
        Set<String> keySet = collect.keySet();
        keySet.forEach(t -> resultList.addAll(collect.get(t)));
        //过滤小计的不要求和
        resultList.stream().filter(t -> !"小计".equals(t.getName())).collect(Collectors.toList());
        Dish dish = new Dish();
        dish.setName("合计");
        dish.setCalories(resultList.stream().filter(t -> !"小计".equals(t.getName())).mapToInt(Dish::getCalories).sum());
        resultList.add(dish);
        System.out.println("按类型分类求和：" + resultList);
    }

    @Test
    public void dishTest1() {
        List<Dish> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Dish dish = new Dish();
            if (i % 2 == 0) {
                dish.setName("照明用电" + i);
                dish.setVegetarian(true);
                dish.setCalories(i);
                dish.setType(new Type(Integer.toString(i), "电"));
            } else {

                dish.setName("水井" + i);
                dish.setVegetarian(true);
                dish.setCalories(i);
                dish.setType(new Type(Integer.toString(i), "水"));
            }
            list.add(dish);
        }

        List<String> collect = list.stream().filter(t -> t.getCalories() > 2).sorted(Comparator.comparing(Dish::getCalories)).map(Dish::getName).collect(Collectors.toList());

    }

    @Test
    public void dishTest3() {
        String s = "{\n" +
                "    \"code\":200,\n" +
                "    \"data\":[\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:00:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278367\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":46.4,\n" +
                "            \"PM10\":65,\n" +
                "            \"PM25\":39\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:01:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278368\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":46,\n" +
                "            \"PM10\":62,\n" +
                "            \"PM25\":39\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:02:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278369\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.3,\n" +
                "            \"PM10\":74,\n" +
                "            \"PM25\":43\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:03:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278370\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.5,\n" +
                "            \"PM10\":71,\n" +
                "            \"PM25\":44\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:04:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278371\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.3,\n" +
                "            \"PM10\":79,\n" +
                "            \"PM25\":41\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:05:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278372\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":65.6,\n" +
                "            \"PM10\":68,\n" +
                "            \"PM25\":42\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:06:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278373\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.5,\n" +
                "            \"PM10\":76,\n" +
                "            \"PM25\":45\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:07:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278374\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.7,\n" +
                "            \"PM10\":85,\n" +
                "            \"PM25\":48\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:08:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278375\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.8,\n" +
                "            \"PM10\":69,\n" +
                "            \"PM25\":41\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:09:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278376\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.7,\n" +
                "            \"PM10\":108,\n" +
                "            \"PM25\":56\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:10:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278377\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.1,\n" +
                "            \"PM10\":106,\n" +
                "            \"PM25\":54\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:11:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278378\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":45.3,\n" +
                "            \"PM10\":134,\n" +
                "            \"PM25\":71\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:12:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278379\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44.2,\n" +
                "            \"PM10\":148,\n" +
                "            \"PM25\":73\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:13:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278380\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.3,\n" +
                "            \"PM10\":113,\n" +
                "            \"PM25\":62\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:14:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278381\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43,\n" +
                "            \"PM10\":103,\n" +
                "            \"PM25\":59\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:15:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278382\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64,\n" +
                "            \"PM10\":97,\n" +
                "            \"PM25\":55\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:16:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278383\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44.1,\n" +
                "            \"PM10\":102,\n" +
                "            \"PM25\":59\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:17:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278384\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44.4,\n" +
                "            \"PM10\":95,\n" +
                "            \"PM25\":55\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:18:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278385\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.7,\n" +
                "            \"PM10\":98,\n" +
                "            \"PM25\":54\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:19:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278386\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":65.5,\n" +
                "            \"PM10\":96,\n" +
                "            \"PM25\":55\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:20:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278387\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.1,\n" +
                "            \"PM10\":91,\n" +
                "            \"PM25\":54\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:21:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278388\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.1,\n" +
                "            \"PM10\":83,\n" +
                "            \"PM25\":51\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:22:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278389\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":67.5,\n" +
                "            \"PM10\":85,\n" +
                "            \"PM25\":52\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:23:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278390\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":70.3,\n" +
                "            \"PM10\":87,\n" +
                "            \"PM25\":51\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:24:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278391\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.3,\n" +
                "            \"PM10\":87,\n" +
                "            \"PM25\":49\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:25:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278392\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.6,\n" +
                "            \"PM10\":85,\n" +
                "            \"PM25\":50\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:26:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278393\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44.7,\n" +
                "            \"PM10\":90,\n" +
                "            \"PM25\":52\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:27:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278394\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":69.8,\n" +
                "            \"PM10\":91,\n" +
                "            \"PM25\":52\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:28:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278395\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44.7,\n" +
                "            \"PM10\":88,\n" +
                "            \"PM25\":49\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:29:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278396\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":45.3,\n" +
                "            \"PM10\":87,\n" +
                "            \"PM25\":49\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:30:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278397\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44.7,\n" +
                "            \"PM10\":84,\n" +
                "            \"PM25\":52\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:31:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278398\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.6,\n" +
                "            \"PM10\":81,\n" +
                "            \"PM25\":50\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:32:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278399\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44,\n" +
                "            \"PM10\":87,\n" +
                "            \"PM25\":48\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:33:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278400\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":46,\n" +
                "            \"PM10\":83,\n" +
                "            \"PM25\":49\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:34:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278401\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.1,\n" +
                "            \"PM10\":83,\n" +
                "            \"PM25\":48\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:35:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278402\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.2,\n" +
                "            \"PM10\":77,\n" +
                "            \"PM25\":47\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:36:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278403\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.6,\n" +
                "            \"PM10\":75,\n" +
                "            \"PM25\":46\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:37:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278404\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":45.5,\n" +
                "            \"PM10\":82,\n" +
                "            \"PM25\":47\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:38:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278405\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":46.6,\n" +
                "            \"PM10\":82,\n" +
                "            \"PM25\":50\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:39:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278406\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.7,\n" +
                "            \"PM10\":80,\n" +
                "            \"PM25\":46\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:40:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278407\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":46.4,\n" +
                "            \"PM10\":75,\n" +
                "            \"PM25\":46\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:41:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278408\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.7,\n" +
                "            \"PM10\":80,\n" +
                "            \"PM25\":47\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:42:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278409\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42,\n" +
                "            \"PM10\":74,\n" +
                "            \"PM25\":46\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:43:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278410\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.1,\n" +
                "            \"PM10\":74,\n" +
                "            \"PM25\":46\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:44:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278411\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.3,\n" +
                "            \"PM10\":77,\n" +
                "            \"PM25\":47\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:45:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278412\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":55.4,\n" +
                "            \"PM10\":72,\n" +
                "            \"PM25\":44\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:46:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278413\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.4,\n" +
                "            \"PM10\":73,\n" +
                "            \"PM25\":44\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:47:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278414\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.4,\n" +
                "            \"PM10\":68,\n" +
                "            \"PM25\":46\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:48:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278415\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":46.8,\n" +
                "            \"PM10\":76,\n" +
                "            \"PM25\":47\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:49:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278416\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.6,\n" +
                "            \"PM10\":70,\n" +
                "            \"PM25\":42\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:50:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278417\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43,\n" +
                "            \"PM10\":67,\n" +
                "            \"PM25\":41\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:51:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278418\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.9,\n" +
                "            \"PM10\":69,\n" +
                "            \"PM25\":45\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:52:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278419\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.3,\n" +
                "            \"PM10\":71,\n" +
                "            \"PM25\":45\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:53:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278420\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":52.5,\n" +
                "            \"PM10\":65,\n" +
                "            \"PM25\":42\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:54:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278421\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44.7,\n" +
                "            \"PM10\":68,\n" +
                "            \"PM25\":40\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:55:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278422\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.4,\n" +
                "            \"PM10\":70,\n" +
                "            \"PM25\":46\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:56:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278423\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.4,\n" +
                "            \"PM10\":72,\n" +
                "            \"PM25\":46\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:57:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278424\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.4,\n" +
                "            \"PM10\":76,\n" +
                "            \"PM25\":46\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:58:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278425\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.4,\n" +
                "            \"PM10\":69,\n" +
                "            \"PM25\":44\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 15:59:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278426\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.5,\n" +
                "            \"PM10\":70,\n" +
                "            \"PM25\":46\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:00:30\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278427\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.4,\n" +
                "            \"PM10\":68,\n" +
                "            \"PM25\":43\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:01:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278428\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.2,\n" +
                "            \"PM10\":69,\n" +
                "            \"PM25\":44\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:02:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278429\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.5,\n" +
                "            \"PM10\":67,\n" +
                "            \"PM25\":44\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:03:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278430\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.5,\n" +
                "            \"PM10\":70,\n" +
                "            \"PM25\":44\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:04:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278431\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.4,\n" +
                "            \"PM10\":93,\n" +
                "            \"PM25\":54\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:05:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278432\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.2,\n" +
                "            \"PM10\":92,\n" +
                "            \"PM25\":56\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:06:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278433\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.4,\n" +
                "            \"PM10\":88,\n" +
                "            \"PM25\":54\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:07:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278434\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.3,\n" +
                "            \"PM10\":86,\n" +
                "            \"PM25\":54\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:08:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278435\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.2,\n" +
                "            \"PM10\":83,\n" +
                "            \"PM25\":50\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:09:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278436\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.4,\n" +
                "            \"PM10\":87,\n" +
                "            \"PM25\":52\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:10:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278437\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.2,\n" +
                "            \"PM10\":85,\n" +
                "            \"PM25\":53\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:11:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278438\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":61.3,\n" +
                "            \"PM10\":91,\n" +
                "            \"PM25\":55\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:12:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278439\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.1,\n" +
                "            \"PM10\":85,\n" +
                "            \"PM25\":54\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:13:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278440\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":67.3,\n" +
                "            \"PM10\":86,\n" +
                "            \"PM25\":53\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:14:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278441\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.5,\n" +
                "            \"PM10\":87,\n" +
                "            \"PM25\":53\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:15:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278442\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.2,\n" +
                "            \"PM10\":94,\n" +
                "            \"PM25\":55\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:16:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278443\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.1,\n" +
                "            \"PM10\":96,\n" +
                "            \"PM25\":57\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:17:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278444\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.4,\n" +
                "            \"PM10\":97,\n" +
                "            \"PM25\":58\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:18:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278445\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":65,\n" +
                "            \"PM10\":88,\n" +
                "            \"PM25\":54\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:19:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278446\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44.1,\n" +
                "            \"PM10\":102,\n" +
                "            \"PM25\":57\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:20:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278447\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44.3,\n" +
                "            \"PM10\":95,\n" +
                "            \"PM25\":56\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:21:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278448\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":45.9,\n" +
                "            \"PM10\":96,\n" +
                "            \"PM25\":54\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:22:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278449\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":51.5,\n" +
                "            \"PM10\":92,\n" +
                "            \"PM25\":54\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:23:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278450\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.1,\n" +
                "            \"PM10\":89,\n" +
                "            \"PM25\":55\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:24:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278451\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.3,\n" +
                "            \"PM10\":93,\n" +
                "            \"PM25\":55\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:25:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278452\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44.7,\n" +
                "            \"PM10\":84,\n" +
                "            \"PM25\":53\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:26:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278453\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.8,\n" +
                "            \"PM10\":86,\n" +
                "            \"PM25\":52\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:27:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278454\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.9,\n" +
                "            \"PM10\":85,\n" +
                "            \"PM25\":51\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:28:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278455\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.2,\n" +
                "            \"PM10\":85,\n" +
                "            \"PM25\":51\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:29:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278456\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.7,\n" +
                "            \"PM10\":87,\n" +
                "            \"PM25\":51\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:30:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278457\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.3,\n" +
                "            \"PM10\":84,\n" +
                "            \"PM25\":51\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:31:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278458\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":48.8,\n" +
                "            \"PM10\":85,\n" +
                "            \"PM25\":52\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:32:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278459\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":65.5,\n" +
                "            \"PM10\":87,\n" +
                "            \"PM25\":50\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:33:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278460\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44.8,\n" +
                "            \"PM10\":83,\n" +
                "            \"PM25\":52\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:34:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278461\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.7,\n" +
                "            \"PM10\":85,\n" +
                "            \"PM25\":51\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:35:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278462\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":68.9,\n" +
                "            \"PM10\":80,\n" +
                "            \"PM25\":50\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:36:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278463\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44.2,\n" +
                "            \"PM10\":84,\n" +
                "            \"PM25\":51\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:37:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278464\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":47.9,\n" +
                "            \"PM10\":81,\n" +
                "            \"PM25\":51\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:38:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278465\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":47.4,\n" +
                "            \"PM10\":87,\n" +
                "            \"PM25\":52\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:39:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278466\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.6,\n" +
                "            \"PM10\":78,\n" +
                "            \"PM25\":49\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:40:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278467\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":43.5,\n" +
                "            \"PM10\":82,\n" +
                "            \"PM25\":52\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:41:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278468\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":50.3,\n" +
                "            \"PM10\":80,\n" +
                "            \"PM25\":51\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:42:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278469\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":68,\n" +
                "            \"PM10\":78,\n" +
                "            \"PM25\":50\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:43:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278470\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.3,\n" +
                "            \"PM10\":80,\n" +
                "            \"PM25\":50\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:44:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278471\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":44.3,\n" +
                "            \"PM10\":79,\n" +
                "            \"PM25\":51\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:45:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278472\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":41.7,\n" +
                "            \"PM10\":78,\n" +
                "            \"PM25\":49\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:46:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278473\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.9,\n" +
                "            \"PM10\":86,\n" +
                "            \"PM25\":52\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:47:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278474\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":45,\n" +
                "            \"PM10\":73,\n" +
                "            \"PM25\":48\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:48:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278475\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.4,\n" +
                "            \"PM10\":76,\n" +
                "            \"PM25\":48\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:49:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278476\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":53.2,\n" +
                "            \"PM10\":72,\n" +
                "            \"PM25\":44\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:50:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278477\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.3,\n" +
                "            \"PM10\":73,\n" +
                "            \"PM25\":47\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:51:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278478\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.4,\n" +
                "            \"PM10\":73,\n" +
                "            \"PM25\":47\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:52:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278479\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.3,\n" +
                "            \"PM10\":73,\n" +
                "            \"PM25\":47\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:53:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278480\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":64.4,\n" +
                "            \"PM10\":74,\n" +
                "            \"PM25\":46\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:54:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278481\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":42.1,\n" +
                "            \"PM10\":77,\n" +
                "            \"PM25\":50\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:55:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278482\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":65.4,\n" +
                "            \"PM10\":73,\n" +
                "            \"PM25\":50\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:56:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278483\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":65.4,\n" +
                "            \"PM10\":75,\n" +
                "            \"PM25\":48\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:57:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278484\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":65.9,\n" +
                "            \"PM10\":80,\n" +
                "            \"PM25\":51\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:58:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278485\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":65.7,\n" +
                "            \"PM10\":69,\n" +
                "            \"PM25\":43\n" +
                "        },\n" +
                "        {\n" +
                "            \"CH4\":-999999,\n" +
                "            \"CO2\":-999999,\n" +
                "            \"CreateTime\":\"2023-04-18 16:59:29\",\n" +
                "            \"DeviceID\":\"03080296\",\n" +
                "            \"HUMI\":-999999,\n" +
                "            \"ID\":\"278486\",\n" +
                "            \"NMHC\":-999999,\n" +
                "            \"NOISE\":74.9,\n" +
                "            \"PM10\":74,\n" +
                "            \"PM25\":45\n" +
                "        }\n" +
                "    ],\n" +
                "    \"status\":200\n" +
                "}\n";
        JSONObject jsonObject = JSON.parseObject(s);
        Integer code = (Integer) jsonObject.get("code");
        if (code == 200) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) jsonObject.get("data");
            if (data.size() > 0) {
                Map<String, Object> map = data.get(0);

                String pm25 = String.valueOf(map.get("PM25"));
                System.out.println(pm25);


            }
        }
    }


    @Test
    public void dishTest4() {
        List<EleWaterVO> list = new ArrayList<>();
        EleWaterVO eleWaterVO1 = new EleWaterVO();
        eleWaterVO1.setEleComeBackName("能耗报表-电表001");
        eleWaterVO1.setEleAddress("经开院区,A-3-6#医技楼,1F,中庭2");
        eleWaterVO1.setEleEnergy(1000.0);
        eleWaterVO1.setEleCost(500000.0);
        eleWaterVO1.setWaterComeBackName("报表-水表004");
        eleWaterVO1.setWaterAddress("经开院区,A-3-6#医技楼,2F,男卫");
        eleWaterVO1.setWaterEnergy(3000.0);
        eleWaterVO1.setWaterCost(4500.0);
        list.add(eleWaterVO1);

        EleWaterVO eleWaterVO2 = new EleWaterVO();
        eleWaterVO2.setEleComeBackName("能耗报表-电表001");
        eleWaterVO2.setEleAddress("经开院区,A-3-6#医技楼,1F,中庭2");
        eleWaterVO2.setEleEnergy(1000.0);
        eleWaterVO2.setEleCost(500000.0);
        eleWaterVO2.setWaterComeBackName("报表水-004");
        eleWaterVO2.setWaterAddress("经开院区,C-2#门诊楼,1F,无性别卫生间");
        eleWaterVO2.setWaterEnergy(0.0);
        eleWaterVO2.setWaterCost(0.0);
        list.add(eleWaterVO2);


        EleWaterVO eleWaterVO3 = new EleWaterVO();
        eleWaterVO3.setEleComeBackName("勿删-测试电表1");
        eleWaterVO3.setEleAddress("经开院区,A-3-6#医技楼,1F,中庭2");
        eleWaterVO3.setEleEnergy(0.0);
        eleWaterVO3.setEleCost(0.0);
        eleWaterVO3.setWaterComeBackName("报表-水表003");
        eleWaterVO3.setWaterAddress("经开院区,A-3-6#医技楼,2F,男卫");
        eleWaterVO3.setWaterEnergy(3000.0);
        eleWaterVO3.setWaterCost(4500.0);
        list.add(eleWaterVO3);


        EleWaterVO eleWaterVO4 = new EleWaterVO();
        eleWaterVO4.setEleComeBackName("勿删-测试电表1");
        eleWaterVO4.setEleAddress("经开院区,A-3-6#医技楼,1F,中庭2");
        eleWaterVO4.setEleEnergy(0.0);
        eleWaterVO4.setEleCost(0.0);
        eleWaterVO4.setWaterComeBackName("报表水-003");
        eleWaterVO4.setWaterAddress("经开院区,C-2#门诊楼,1F,无性别卫生间");
        eleWaterVO4.setWaterEnergy(0.0);
        eleWaterVO4.setWaterCost(0.0);
        list.add(eleWaterVO4);


        List<EleWaterVO> collect = list.stream().map(x -> {
            EleWaterVO eleWaterVO = new EleWaterVO();
            eleWaterVO.setEleComeBackName(x.getEleComeBackName());
            eleWaterVO.setEleAddress(x.getEleAddress());
            eleWaterVO.setEleEnergy(x.getEleEnergy());
            eleWaterVO.setEleCost(x.getEleCost());

            return eleWaterVO;
        }).collect(Collectors.toList());

        System.out.println(JSON.toJSON(collect));
    }

    public void removeDuplicate(List<EleWaterVO> personVoList) {
        if (CollectionUtils.isEmpty(personVoList)) {
            return;
        }

        for (int i = 0; i < personVoList.size(); i++) {
            EleWaterVO personVo = personVoList.get(i);
            String a00 = personVo.getWaterComeBackName();
            for (int j = i + 1; j < personVoList.size(); j++) {
                EleWaterVO vo = personVoList.get(j);
                if (StringUtils.endsWithIgnoreCase(a00, vo.getWaterComeBackName())) {
                    personVoList.remove(j);
                    j--;
                }
            }
        }
    }

    public static void main(String[] args) {
        //obj instance class obj是引用对象 class是类或者接口
        //obj是class的实例对象
        //obj是class的直接子类或者间接子类
        //obj是class接口的实现类
        User user = new User();
        if (user instanceof User) {
            System.out.println(true);
        }
        if (user instanceof Object) {
            System.out.println(true);
        }
        Double d = 0.3;
        if (d instanceof Double) {
            System.out.println(true);
        }
        ArrayList arrayList = new ArrayList();
        if (arrayList instanceof List) {
            System.out.println(true);
        }
    }

}
