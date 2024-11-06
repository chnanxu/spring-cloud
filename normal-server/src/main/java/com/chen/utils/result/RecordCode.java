package com.chen.utils.result;

public enum RecordCode implements ResultCode{

    NEW_DIARYBOOK_SUCCESS(true,0,"创建成功"),
    NEW_DIARYBOOK_FAILURE(false,1,"创建失败，您的数量已达上限")
    ;

    boolean success;
    int code;
    String message;

    RecordCode(boolean success, int code, String message) {
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
