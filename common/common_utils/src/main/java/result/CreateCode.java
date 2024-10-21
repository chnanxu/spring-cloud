package result;

public enum CreateCode implements ResultCode {

    //发布成功
    CREATE_SUCCESS(true,0,"发布成功"),

    // 含有敏感词
    CONTAIN_SENSITIVEWORD(false,1,"含有敏感词"),

    // 内容违规
    CONTENT_IRREGULARITIES(false,1,"内容违规"),


    ;

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
