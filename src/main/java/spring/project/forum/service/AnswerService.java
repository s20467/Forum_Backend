package spring.project.forum.service;

import org.springframework.data.domain.Page;
import spring.project.forum.api.v1.dto.AnswerDto;
import spring.project.forum.api.v1.dto.AnswerDtoAdmin;
import spring.project.forum.model.Answer;

import java.util.List;

public interface AnswerService {
    List<Answer> getAll();

    Page<Answer> getAll(Integer pageNum, Integer pageSize, String sortBy);

    Answer getById(Integer answerId);

    void deleteById(Integer answerId);

    Answer createAnswerForQuestionAdmin(Integer questionId, AnswerDtoAdmin answerDtoAdmin);

    Answer createAnswerForQuestion(Integer questionId, AnswerDto answerDto);

    Answer updateAnswerAdmin(Integer answerId, AnswerDtoAdmin answerDtoAdmin);

    Answer updateAnswer(Integer answerId, AnswerDto answerDto);

    Answer upVote(Integer answerId);

    Answer downVote(Integer answerId);

    Answer unUpVote(Integer answerId);

    Answer unDownVote(Integer answerId);

    List<Answer> getByQuestion(Integer questionId);

    Page<Answer> getByQuestion(Integer questionId, Integer pageNum, Integer pageSize, String sortBy);

    List<Answer> getByAuthor(String username);

    Page<Answer> getByAuthor(String username, Integer pageNum, Integer pageSize, String sortBy);
}
