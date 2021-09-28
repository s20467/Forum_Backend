package spring.project.forum.service;

import org.springframework.data.domain.Page;
import spring.project.forum.api.v1.dto.QuestionDto;
import spring.project.forum.model.Question;

import java.util.List;

public interface QuestionService {
    Question getById(Integer questionId);

    void deleteById(Integer questionId);

    Question updateQuestion(Integer questionId, QuestionDto questionDto);

    Question upVote(Integer questionId);

    Question downVote(Integer questionId);

    List<Question> getAll();

    Page<Question> getAll(Integer pageNum, Integer pageSize, String sortBy);

    Question createQuestion(QuestionDto questionDto);

    Question closeQuestion(Integer questionId);

    List<Question> getByAuthor(String username);

    Page<Question> getByAuthor(String username, Integer pageNum, Integer pageSize, String sortBy);

    List<Question> getWithoutBestAnswer(String username);

    Page<Question> getWithoutBestAnswer(String username, Integer pageNum, Integer pageSize, String sortBy);

    Question setBestAnswer(Integer questionId, Integer answerId);

    Question unsetBestAnswer(Integer questionId);
}
