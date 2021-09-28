package spring.project.forum.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JWTVerificationExceptionHandler {

    public static void handleJWTVerificationException(JWTVerificationException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("error", exception.getMessage());
        if (exception instanceof TokenExpiredException)
            response.setStatus(UNAUTHORIZED.value());
        else
            response.setStatus(FORBIDDEN.value());
        Map<String, String> error = Map.of("error_message", exception.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
