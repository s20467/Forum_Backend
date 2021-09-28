package spring.project.forum.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import spring.project.forum.api.v1.dto.UserCredentialsDto;
import spring.project.forum.model.security.User;

public interface UserService extends UserDetailsService {
    User create(UserCredentialsDto userCredentialsDto);
}
