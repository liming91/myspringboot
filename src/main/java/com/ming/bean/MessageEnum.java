package com.ming.bean;

public enum MessageEnum {
    E00(0, "请求成功"),
    E01(1,"请求失败");

    private final int index;
    private final String value;

    private MessageEnum(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }
}
