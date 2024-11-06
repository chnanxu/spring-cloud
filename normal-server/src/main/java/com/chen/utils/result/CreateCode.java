package com.chen.utils.result;

public enum CreateCode implements ResultCode {

    //发布成功
    CREATE_SUCCESS(true,0,"发布成功"),

    // 含有敏感词
    CONTAIN_SENSITIVEWORD(false,1,"含有敏感词"),

    // 内容违规
    CONTENT_IRREGULARITIES(false,1,"内容违规"),

    //作品恢复失败
    RECOVER_FAILURE(false,1,"恢复失败，该作品已被永久封禁"),

    //作品恢复成功
    RECOVER_SUCCESS(true, 0, "恢复成功，请等待重新审核");

    boolean success;
    int code;
    String message;

    CreateCode(boolean success, int code, String message) {
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
