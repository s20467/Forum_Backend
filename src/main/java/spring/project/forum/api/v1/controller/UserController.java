package spring.project.forum.api.v1.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import spring.project.forum.api.v1.dto.PasswordDto;
import spring.project.forum.api.v1.dto.UserDto;
import spring.project.forum.api.v1.dto.UserExcludePasswordDto;
import spring.project.forum.exception.JWTVerificationExceptionHandler;
import spring.project.forum.model.security.User;
import spring.project.forum.service.JwtService;
import spring.project.forum.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("users")
    public List<User> getUsers() {
        return userService.getAll();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("users/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("users/{username}/check-username-availability")
    public boolean checkUsernameAvailability(@PathVariable("username") String username) {
        return userService.checkUsernameAvailability(username);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("users/create")
    public void createUser(@Valid @RequestBody UserDto userDto) {
        userService.createUser(userDto);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isAccountOwner(authentication, #username))")
    @DeleteMapping("users/{username}")
    public void deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isAccountOwner(authentication, #username))")
    @PatchMapping("users/{username}")
    public void editUserExcludingPassword(@PathVariable("username") String username, @Valid @RequestBody UserExcludePasswordDto userDto) {
        userService.editUserExcludingPassword(username, userDto);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isAccountOwner(authentication, #username))")
    @PostMapping("users/{username}/change-password")
    public void editUserPassword(@PathVariable("username") String username, @Valid @RequestBody PasswordDto userPasswordDto) {
        userService.editUserPassword(username, userPasswordDto.getPassword());
    }

    @PreAuthorize("permitAll()")
    @GetMapping("refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                User user = jwtService.getUserFromRefreshToken(token);
                String new_access_token = jwtService.getStringTokenFromUser(user, (60L * 60 * 1000), request.getRequestURI(), true);
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
