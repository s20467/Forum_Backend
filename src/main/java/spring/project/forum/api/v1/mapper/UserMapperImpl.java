package spring.project.forum.api.v1.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import spring.project.forum.api.v1.dto.UserCredentialsDto;
import spring.project.forum.model.security.User;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User userCredentialsDtoToUser(UserCredentialsDto userCredentialsDto) {
        return User.builder()
                .username(userCredentialsDto.getUsername())
                .password(passwordEncoder.encode(userCredentialsDto.getPassword()))
                .build();
    }
}
