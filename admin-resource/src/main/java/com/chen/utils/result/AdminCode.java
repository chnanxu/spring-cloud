package com.chen.utils.result;

public enum AdminCode implements ResultCode{

    GET_SUCCESS(true,0,"获取成功")
    ;

    final boolean success;
    final int code;
    final String message;

    AdminCode(boolean success,int code,String message){
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
