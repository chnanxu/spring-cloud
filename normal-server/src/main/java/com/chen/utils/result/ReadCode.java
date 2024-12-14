package com.chen.utils.result;

public enum ReadCode implements ResultCode{

    BOOK_NULL(false,1,"书籍数据为空，获取失败")
    ;

    boolean success;
    int code;
    String message;

    ReadCode(boolean success,int code,String message){
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
