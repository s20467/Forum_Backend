package spring.project.forum.service;

import org.springframework.data.domain.Page;
import spring.project.forum.model.Question;

import java.util.List;

public interface QuestionService {
    Question getById(Integer questionId);
    Question deleteById(Integer questionId);
    List<Question> getAll();
    Page<Question> getAll(Integer pageNum, Integer pageSize, String sortBy);
    Question createQuestion(Question question);
    Question closeQuestion(Integer questionId);
    List<Question> getByAuthor(String username);
    Page<Question> getByAuthor(String username, Integer pageNum, Integer pageSize, String sortBy);
    List<Question> getWithoutBestAnswer(String username);
    Page<Question> getWithoutBestAnswer(String username, Integer pageNum, Integer pageSize, String sortBy);
}
