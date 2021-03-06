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
public class QuestionDto {

    @NotBlank(message = "Title is mandatory")
    @Size(min = 10, max = 255, message = "Title must be at least 10 characters long")
    String title;

    String content;
}
