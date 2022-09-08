package com.ming.entities;

import lombok.Data;

/**
 * @author liming
 * @create 2022/5/24 15:02
 */
@Data
public class AA {
    private String aId;

    public Bb getBb() {
        return new Bb();
    }

    @Data
    public class Bb{
        private String bId;

    }
}
