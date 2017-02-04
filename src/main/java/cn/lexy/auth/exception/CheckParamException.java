package cn.lexy.auth.exception;

/**
 * Created by john on 16/8/7.
 */
@SuppressWarnings("Since15")
public class CheckParamException extends RuntimeException {
    public CheckParamException() {
        super();
    }

    public CheckParamException(String message) {
        super(message);
    }

    public CheckParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckParamException(Throwable cause) {
        super(cause);
    }

    protected CheckParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
