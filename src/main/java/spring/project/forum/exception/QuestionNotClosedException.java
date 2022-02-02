package spring.project.forum.exception;

public class QuestionNotClosedException extends RuntimeException{
    public QuestionNotClosedException() {
    }

    public QuestionNotClosedException(String message) {
        super(message);
    }

    public QuestionNotClosedException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionNotClosedException(Throwable cause) {
        super(cause);
    }

    public QuestionNotClosedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
