package spring.project.forum.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDtoAdmin {

    @NotBlank(message = "Content is mandatory")
    @Size(min = 10, message = "Content must be at least 10 characters long")
    String content;

    @NotNull(message = "Creation date is mandatory")
    String createdAt;

    String closedAt;

    @NotNull(message = "Author date is mandatory")
    String author;
}
