package com.chen.utils.result;

public enum CommonCode implements ResultCode {

    // 非法参数
    INVALID_PARAM(false,-1,"非法参数！"),
    // 操作成功
    SUCCESS(true,0,"操作成功！"),
    // 操作失败
    FAIL(false,1,"操作失败！"),
    ;

    boolean success;
    int code;
    String message;

    CommonCode(boolean success, int code, String message) {
        this.success=success;
        this.code=code;
        this.message=message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
