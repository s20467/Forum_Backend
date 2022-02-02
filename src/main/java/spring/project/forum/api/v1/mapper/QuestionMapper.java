package spring.project.forum.api.v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import spring.project.forum.api.v1.dto.QuestionDto;
import spring.project.forum.api.v1.dto.QuestionDtoAdmin;
import spring.project.forum.model.Question;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    Question questionDtoToQuestion(QuestionDto questionDto);

    @Mapping(target = "author", ignore = true)
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "stringToLocalDate")
    @Mapping(source = "closedAt", target = "closedAt", qualifiedByName = "stringToLocalDate")
    Question questionDtoAdminToQuestion(QuestionDtoAdmin questionDtoAdmin);


    @Named("stringToLocalDate")
    public static LocalDate stringToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (date == null)
            return null;
        return LocalDate.parse(date, formatter);
    }
}
