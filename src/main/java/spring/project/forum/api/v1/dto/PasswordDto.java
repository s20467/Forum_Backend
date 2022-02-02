package spring.project.forum.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordDto {

    @NotBlank(message = "Password is mandatory")
    @Size(min = 5, max = 20, message = "Password must be 5-20 characters long")
    String password;
}
