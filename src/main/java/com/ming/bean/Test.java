package com.ming.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class Test {
    private String id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String time;


}
