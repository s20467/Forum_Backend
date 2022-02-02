package spring.project.forum.exception;

public class UsernameAlreadyUsedException extends RuntimeException {
    public UsernameAlreadyUsedException() {
    }

    public UsernameAlreadyUsedException(String message) {
        super(message);
    }

    public UsernameAlreadyUsedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyUsedException(Throwable cause) {
        super(cause);
    }

    public UsernameAlreadyUsedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
