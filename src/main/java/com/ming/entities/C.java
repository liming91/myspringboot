package com.ming.entities;

import java.util.concurrent.Callable;

public class C implements Callable {
    int count =0;
    @Override
    public Object call() throws Exception {
        for (int i=0;i<3;i++){
            count +=i;
        }
        return count;
    }
}
