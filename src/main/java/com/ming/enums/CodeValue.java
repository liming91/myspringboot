package com.ming.enums;

/**
 * @Author liming
 * @Date 2023/10/31 17:46
 */
public enum CodeValue {

    CODE_VALUE1(1,10),
    CODE_VALUE2(2,20),

    CODE_DEFAULT(3,30);

    private int code;

    private int value;


    CodeValue(int code, int value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }


    public static int getCode(int code){
        CodeValue[] values = CodeValue.values();
        for (CodeValue value : values) {
            if(code==value.code){
                return value.value;
            }
        }
        return 0;
    }


    public static void main(String[] args) {
        int code1 = CodeValue.getCode(1);
        System.out.println(code1);
    }
}
