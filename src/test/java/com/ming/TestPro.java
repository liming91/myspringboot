package com.ming;

import com.ming.entities.*;
import com.ming.util.PercentUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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


    public static Double billingFormula(double number , EnergyType decimalPlace ) {


        Double fm = 0.00;
        switch (decimalPlace) {
            case  water:
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

        return  fm;

    }
}
