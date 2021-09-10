package spring.project.forum.service;

import org.springframework.data.domain.Page;
import spring.project.forum.model.PostAnswer;

import java.util.List;

public interface PostAnswerService {
    List<PostAnswer> getAll();
    Page<PostAnswer> getAll(Integer pageNum, Integer pageSize, String sortBy);
    List<PostAnswer> getAnswersByQuestion(Integer questionId);
    Page<PostAnswer> getAnswersByQuestion(Integer questionId, Integer pageNum, Integer pageSize, String sortBy);
    List<PostAnswer> getAnswersByAuthor(String username);
    Page<PostAnswer> getAnswersByAuthor(String username, Integer pageNum, Integer pageSize, String sortBy);
}
