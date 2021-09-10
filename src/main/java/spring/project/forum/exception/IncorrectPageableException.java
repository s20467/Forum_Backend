package spring.project.forum.exception;

public class IncorrectPageableException extends RuntimeException {
    public IncorrectPageableException() {
    }

    public IncorrectPageableException(String message) {
        super(message);
    }

    public IncorrectPageableException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectPageableException(Throwable cause) {
        super(cause);
    }

    public IncorrectPageableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
