package spring.project.forum.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import spring.project.forum.api.v1.dto.UserDto;
import spring.project.forum.api.v1.dto.UserExcludePasswordDto;
import spring.project.forum.model.security.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAll();

    boolean checkUsernameAvailability(String username);

    User createUser(UserDto userDto);

    void deleteUser(String username);

    User editUserExcludingPassword(String username, UserExcludePasswordDto userDto);

    void editUserPassword(String username, String newPassword);

    User getByUsername(String username);
}
