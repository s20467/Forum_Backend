package spring.project.forum.service;

import org.springframework.data.domain.Page;
import spring.project.forum.model.PostQuestion;

import java.util.List;

public interface PostQuestionService {
    PostQuestion getById(Integer questionId);
    PostQuestion deleteById(Integer questionId);
    List<PostQuestion> getAll();
    Page<PostQuestion> getAll(Integer pageNum, Integer pageSize, String sortBy);
    PostQuestion createQuestion(PostQuestion question);
    PostQuestion closeQuestion(Integer questionId);
    List<PostQuestion> getByAuthor(String username);
    Page<PostQuestion> getByAuthor(String username, Integer pageNum, Integer pageSize, String sortBy);
    List<PostQuestion> getWithoutBestAnswer(String username);
    Page<PostQuestion> getWithoutBestAnswer(String username, Integer pageNum, Integer pageSize, String sortBy);
}
