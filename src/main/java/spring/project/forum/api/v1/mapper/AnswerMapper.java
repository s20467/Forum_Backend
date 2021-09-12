package spring.project.forum.api.v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import spring.project.forum.api.v1.dto.AnswerDto;
import spring.project.forum.model.Answer;

@Mapper
public interface AnswerMapper {
    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    Answer answerDtoToAnswer(AnswerDto answerDto);
}
