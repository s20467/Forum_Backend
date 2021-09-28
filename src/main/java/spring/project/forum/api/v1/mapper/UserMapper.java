package spring.project.forum.api.v1.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import spring.project.forum.api.v1.dto.UserCredentialsDto;
import spring.project.forum.model.security.User;


public interface UserMapper {

    User userCredentialsDtoToUser(UserCredentialsDto userCredentialsDto);

}