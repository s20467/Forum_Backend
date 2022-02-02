package spring.project.forum.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.project.forum.api.v1.dto.UserDto;
import spring.project.forum.api.v1.dto.UserExcludePasswordDto;
import spring.project.forum.api.v1.mapper.UserMapper;
import spring.project.forum.exception.ResourceNotFoundException;
import spring.project.forum.exception.UsernameAlreadyUsedException;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.security.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));
    }

    @Override
    public boolean checkUsernameAvailability(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }

    @Override
    public User createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername()))
            throw new UsernameAlreadyUsedException("User with username " + userDto.getUsername() + " already exists");
        return userRepository.save(userMapper.userDtoToUser(userDto));
    }

    @Override
    public void deleteUser(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            throw new ResourceNotFoundException("User with username " + username + " not found");
        User user = userOptional.get();
        user.getAskedQuestions().forEach(question -> question.setAuthor(null));
        user.setAskedQuestions(null);
        user.getGivenAnswers().forEach(answer -> answer.setAuthor(null));
        user.setGivenAnswers(null);
        user.getUpVotedQuestions().forEach(question -> question.getUpVotes().remove(user));
        user.setUpVotedQuestions(null);
        user.getDownVotedQuestions().forEach(question -> question.getDownVotes().remove(user));
        user.setDownVotedQuestions(null);
        user.getUpVotedAnswers().forEach(answer -> answer.getUpVotes().remove(user));
        user.setUpVotedAnswers(null);
        user.getDownVotedAnswers().forEach(answer -> answer.getDownVotes().remove(user));
        user.setDownVotedAnswers(null);
        userRepository.delete(userRepository.save(user));
    }

    @Override
    public User editUserExcludingPassword(String username, UserExcludePasswordDto userDto) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            throw new ResourceNotFoundException("User with username " + username + " not found");
        if (!username.equals(userDto.getUsername()) && userRepository.existsByUsername(userDto.getUsername()))
            throw new UsernameAlreadyUsedException("User with username " + userDto.getUsername() + " already exists");
        User user = userOptional.get();
        user.setUsername(userDto.getUsername());
        return userRepository.save(user);
    }

    @Override
    public void editUserPassword(String username, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            throw new ResourceNotFoundException("User with username " + username + " not found");
        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
