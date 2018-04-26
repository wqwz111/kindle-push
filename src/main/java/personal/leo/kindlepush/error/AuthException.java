package personal.leo.kindlepush.error;

public class AuthException extends RuntimeException {
    public AuthException(Throwable e) {
        super(e);
    }

    public AuthException(String msg, Throwable e) {
        super(msg, e);
    }

    public AuthException(String msg) {
        super(msg);
    }
}
