package spring.project.forum.service;

import org.springframework.data.domain.Page;
import spring.project.forum.model.Answer;

import java.util.List;

public interface AnswerService {
    List<Answer> getAll();
    Page<Answer> getAll(Integer pageNum, Integer pageSize, String sortBy);
    Answer getById(Integer answerId);
    Answer deleteById(Integer answerId);
    Answer createAnswer(Answer answer);
    Answer updateAnswerContent(Integer answerId, Answer answer);
    Answer setAsBestAnswer(Integer answerId);
    Answer unsetAsBestAnswer(Integer answerId);
    Answer upVote(Integer answerId);
    Answer downVote(Integer answerId);
    List<Answer> getByQuestion(Integer questionId);
    Page<Answer> getByQuestion(Integer questionId, Integer pageNum, Integer pageSize, String sortBy);
    List<Answer> getByAuthor(String username);
    Page<Answer> getByAuthor(String username, Integer pageNum, Integer pageSize, String sortBy);
}
