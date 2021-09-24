package com.ming.bean;

public class GenerateResult {
    public static Result genDataSuccessResult(Object data){
    return Result.createDataResult(MessageEnum.E00.getIndex(),MessageEnum.E00.getValue(),data);
    }

    /**
     * 返回成功数据（不带 data）
     * @param messageEnum
     * @return
     */
    public static Result genSuccessResult(MessageEnum messageEnum){
        return Result.createResult(messageEnum.getIndex(), messageEnum.getValue());
    }
}
