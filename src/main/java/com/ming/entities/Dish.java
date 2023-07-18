package com.ming.entities;

import lombok.Data;

@Data
public class Dish {
    private String name;
    private boolean vegetarian;
    private int calories;
    private Type type;
    private int sum;
    private Double a;
}
