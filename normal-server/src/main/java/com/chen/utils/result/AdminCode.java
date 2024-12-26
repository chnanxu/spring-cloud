package com.chen.utils.result;

public enum AdminCode implements ResultCode {

    GET_SUCCESS(true,0,"获取成功"),
    TAKEOFF_SUCCESS(true,0,"下架成功"),
    TAKEOFF_FAILURE(false,1,"下架失败，作品可能不存在"),
    REFUSE_SUCCESS(true,0,"回退成功"),
    REFUSE_FAILURE(false,1,"回退失败,作品可能不存在"),
    DELETE_SUCCESS(true,0,"删除成功"),
    DELETE_FAILURE(false,1,"删除失败，作品可能不存在"),
    RECOVER_SUCCESS(true,0,"恢复成功"),
    RECOVER_FAILURE(false,1,"恢复失败，作品可能不存在"),
    ;

    final boolean success;
    final int code;
    final String message;

    AdminCode(boolean success, int code, String message){
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
