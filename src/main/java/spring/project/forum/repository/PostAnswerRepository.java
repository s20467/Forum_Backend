package spring.project.forum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.project.forum.model.PostAnswer;
import spring.project.forum.model.PostQuestion;
import spring.project.forum.model.security.User;

import java.util.List;

public interface PostAnswerRepository extends JpaRepository<PostAnswer, Integer> {

    Page<PostAnswer> findAllByTargetQuestion(Pageable pageable, PostQuestion postQuestion);
    List<PostAnswer> findAllByTargetQuestion(PostQuestion postQuestion);
    Page<PostAnswer> findAllByAuthor(Pageable pageable, User user);
    List<PostAnswer> findAllByAuthor(User user);
}
