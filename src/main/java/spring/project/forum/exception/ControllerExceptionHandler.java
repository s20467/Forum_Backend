package spring.project.forum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import spring.project.forum.exception.ResourceNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
    public void handleResourceNotFoundException(ResourceNotFoundException exc, WebRequest webRequest){}

    @ExceptionHandler(PostQuestionAlreadyClosedException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Question already closed")
    public void handlePostQuestionAlreadyClosedException(PostQuestionAlreadyClosedException exc, WebRequest webRequest){}

    @ExceptionHandler(IncorrectPageableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Incorrect pagination arguments")
    public void handleIncorrectPageableException(IncorrectPageableException exc, WebRequest webRequest){
    }
}
