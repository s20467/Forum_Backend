package spring.project.forum.api.v1.mapper;

import spring.project.forum.api.v1.dto.UserDto;
import spring.project.forum.model.security.User;

public interface UserMapper {
    User userDtoToUser(UserDto userDto);
}
