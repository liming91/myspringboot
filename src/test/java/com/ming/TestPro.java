package com.ming;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ming.entities.*;
import com.ming.util.PercentUtil;
import org.assertj.core.util.Lists;
import org.json.JSONObject;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

public class TestPro {

    @Test
    public void lineTest44() throws ExecutionException, InterruptedException {
//        A a1 = new A();
//        A a2 = new A();
//        a1.start();
//        a2.start();


//        C c = new C();
//        FutureTask task = new FutureTask(c);
//        Thread t = new Thread(task);
//        t.start();
//        Object o = task.get();
//        System.out.println("总和："+o);

//        DataTrendVO d1 = new DataTrendVO("1",10.1);
//        DataTrendVO d2 = new DataTrendVO("2",20.1);
//        List<DataTrendVO> list = new ArrayList<>();
//        list.add(d1);
//        list.add(d2);
//        Double reduce = list.stream().map(DataTrendVO::getDataValue).reduce(0.00, (a, b) -> a + b);
//
//        System.out.println(reduce);
//        Double fm = 0.00;
//        fm=122.18*0.56;
//        System.out.println(fm);
//        Double aDouble = billingFormula(34.04, EnergyType.electricity);
//        System.out.println(aDouble);
        Double ac = PercentUtil.SC(33.04, 10.23);
        System.out.println(ac);
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


}
