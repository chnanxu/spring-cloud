package com.chen.utils.result;

public enum PageCode implements ResultCode{

    //评论成功
    COMMENT_SUCCESS(true,0,"评论成功"),

    //含有敏感词
    COMMENT_SENSITIVE_WORD(false,1,"评论含有敏感词"),

    //账号被封禁，无法评论
    COMMENT_DISABLE(false,1,"账号被封禁，无法评论"),
    //支持作者
    SUPPORT_SUCCESS(true,0,"感谢您的支持！"),
    //余额不足
    SUPPORT_FAILURE(false,1,"余额不足,请充值!"),
    //举报成功
    REPORT_SUCCESS(true,0,"举报已提交，请等待反馈"),
    //举报失败
    REPORT_FAILURE(false,1,"举报失败，请重新尝试")
    ;

    boolean success;
    int code;
    String message;

    PageCode(boolean success,int code,String message){
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
