package spring.project.forum.api.v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import spring.project.forum.api.v1.dto.AnswerDtoAdmin;
import spring.project.forum.model.Answer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper
public interface AnswerMapperAdmin {
    AnswerMapperAdmin INSTANCE = Mappers.getMapper(AnswerMapperAdmin.class);

    @Mapping(target = "author", ignore = true)
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "stringToLocalDate")
    Answer answerDtoAdminToAnswer(AnswerDtoAdmin answerDto);

    @Named("stringToLocalDate")
    public static LocalDate stringToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(date == null)
            return null;
        return LocalDate.parse(date, formatter);
    }
}
