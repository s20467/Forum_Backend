package spring.project.forum.api.v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import spring.project.forum.api.v1.dto.QuestionDto;
import spring.project.forum.model.Question;

@Mapper
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    Question questionDtoToQuestion(QuestionDto questionDto);
}
