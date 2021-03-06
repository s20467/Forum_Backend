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
public class AnswerDto {

    @NotBlank(message = "Content is mandatory")
    @Size(min = 10, message = "Content must be at least 10 characters long")
    String content;
}
