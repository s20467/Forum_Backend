package spring.project.forum.exception;

public class PostQuestionAlreadyClosedException extends RuntimeException{
    public PostQuestionAlreadyClosedException() {
    }

    public PostQuestionAlreadyClosedException(String message) {
        super(message);
    }

    public PostQuestionAlreadyClosedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostQuestionAlreadyClosedException(Throwable cause) {
        super(cause);
    }

    public PostQuestionAlreadyClosedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
