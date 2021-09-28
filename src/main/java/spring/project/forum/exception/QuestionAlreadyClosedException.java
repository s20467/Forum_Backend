package spring.project.forum.exception;

public class QuestionAlreadyClosedException extends RuntimeException {
    public QuestionAlreadyClosedException() {
    }

    public QuestionAlreadyClosedException(String message) {
        super(message);
    }

    public QuestionAlreadyClosedException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionAlreadyClosedException(Throwable cause) {
        super(cause);
    }

    public QuestionAlreadyClosedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
