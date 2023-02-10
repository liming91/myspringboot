package com.ming.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class GrantUser implements Serializable {
    private String userId;

    private String name;
}
