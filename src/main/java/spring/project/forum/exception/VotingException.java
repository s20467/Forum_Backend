package spring.project.forum.exception;

public class VotingException extends RuntimeException {
    public VotingException() {
    }

    public VotingException(String message) {
        super(message);
    }

    public VotingException(String message, Throwable cause) {
        super(message, cause);
    }

    public VotingException(Throwable cause) {
        super(cause);
    }

    public VotingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
