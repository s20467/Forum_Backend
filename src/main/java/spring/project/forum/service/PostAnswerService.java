package spring.project.forum.service;

import org.springframework.data.domain.Page;
import spring.project.forum.model.PostAnswer;

import java.util.List;

public interface PostAnswerService {
    List<PostAnswer> getAll();
    Page<PostAnswer> getAll(Integer pageNum, Integer pageSize, String sortBy);
    PostAnswer getById(Integer answerId);
    PostAnswer deleteById(Integer answerId);
    PostAnswer createAnswer(PostAnswer answer);
    List<PostAnswer> getByQuestion(Integer questionId);
    Page<PostAnswer> getByQuestion(Integer questionId, Integer pageNum, Integer pageSize, String sortBy);
    List<PostAnswer> getByAuthor(String username);
    Page<PostAnswer> getByAuthor(String username, Integer pageNum, Integer pageSize, String sortBy);
}
