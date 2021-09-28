package spring.project.forum.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.project.forum.api.v1.dto.UserCredentialsDto;
import spring.project.forum.api.v1.mapper.UserMapper;
import spring.project.forum.exception.UsernameDuplicateException;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.security.UserRepository;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }

    @Override
    public User create(UserCredentialsDto userCredentialsDto) {
        if (userRepository.findByUsername(userCredentialsDto.getUsername()).isPresent())
            throw new UsernameDuplicateException("User with username '" + userCredentialsDto.getUsername() + "' already exists");

        User user = userMapper.userCredentialsDtoToUser(userCredentialsDto);
        return userRepository.save(user);
    }
}
