package com.ming.util.http;


import com.ming.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author liming
 * @Date 2022/9/2 9:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;

    private String msg;

    private T data;

    private boolean success;

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMessage();
    }

    public Result(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMessage();
        this.data = data;
    }


    public static Result success() {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMessage());
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public static Result success(boolean success, int code, String message) {
        Result result = new Result();
        result.setSuccess(success);
        result.setCode(code);
        result.setMsg(message);
        return result;
    }


    /**
     * 返回成功数据（不带 data）
     *
     * @param resultCode
     * @return
     */
    public static Result success(ResultCode resultCode) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(resultCode.getCode());
        result.setMsg(resultCode.getMessage());
        return result;
    }


    public static Result failure(ResultCode resultCode) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(resultCode.getCode());
        result.setMsg(resultCode.getMessage());
        return result;
    }

    public static Result failure(int code, String message) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(message);
        return result;
    }


}