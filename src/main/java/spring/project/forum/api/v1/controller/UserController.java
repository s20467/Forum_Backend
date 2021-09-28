package spring.project.forum.api.v1.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import spring.project.forum.api.v1.dto.UserCredentialsDto;
import spring.project.forum.exception.JWTVerificationExceptionHandler;
import spring.project.forum.model.security.User;
import spring.project.forum.service.JwtService;
import spring.project.forum.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(UserCredentialsDto userCredentialsDto) {
        userService.create(userCredentialsDto);
    }

    @GetMapping("refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                User user = jwtService.getUserFromRefreshToken(token);
                String new_access_token = jwtService.getStringTokenFromUser(user, (10L * 60 * 1000), request.getRequestURI(), true);
                Map<String, String> tokens = Map.of("access_token", new_access_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (JWTVerificationException exception) {
                JWTVerificationExceptionHandler.handleJWTVerificationException(exception, request, response);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No refresh token attached in authorization header");
        }
    }
}
