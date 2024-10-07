package com.chen.utils.result;

public enum UserCode implements ResultCode {


    REGCHECKFAILURE(true,0,"验证码错误"),
    REGISTSUCCESS(true,0,"注册成功"),
    LOGINSUCCESS(true,0,"登录成功"),
    USEREXIST(true,21,"账号已存在"),
    NOLOGIN(true,22,"没有登录!"),
    PASSWORDFAILURE(true,23,"账号信息错误"),

    NOPERMISSION(false,403,"没有权限");
    //



    boolean success;
    int code;
    String message;

    UserCode(boolean success, int code, String message){
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
