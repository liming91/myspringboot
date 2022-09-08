package com.ming.enums;

import lombok.Data;

/**
 * @Author liming
 * @Date 2022/9/2 10:49
 */
public enum ResultCode {
    /**
     * 成功状态码
     */
    SUCCESS(1, "成功"),

    /**
     * 参数错误 1001-1999
     */
    PARAM_IS_INVALID(1001, "参数无效"),

    /**
     * 参数错误 1001-1999
     */
    PARAM_IS_BLANK(1002, "参数为空"),
    E02(200002, "添加成功"),
    E03(200003, "添加失败"),
    E04(200004, "修改成功");

    private Integer code;

    private String message;

    private ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
