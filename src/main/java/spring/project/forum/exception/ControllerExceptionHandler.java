package spring.project.forum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
    public void handleResourceNotFoundException(ResourceNotFoundException exc, WebRequest webRequest){}

    @ExceptionHandler(QuestionAlreadyClosedException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Question already closed")
    public void handleQuestionAlreadyClosedException(QuestionAlreadyClosedException exc, WebRequest webRequest){}

    @ExceptionHandler(IncorrectPageableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Incorrect pagination arguments")
    public void handleIncorrectPageableException(IncorrectPageableException exc, WebRequest webRequest){
    }
}
