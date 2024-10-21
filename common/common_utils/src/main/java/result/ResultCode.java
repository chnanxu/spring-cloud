package result;

public interface ResultCode {
    boolean success();

    int code();

    String message();
}
