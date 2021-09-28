package spring.project.forum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.project.forum.model.Answer;
import spring.project.forum.model.Question;
import spring.project.forum.model.security.User;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    Page<Answer> findAllByTargetQuestion(Pageable pageable, Question question);

    List<Answer> findAllByTargetQuestion(Question question);

    Page<Answer> findAllByAuthor(Pageable pageable, User user);

    List<Answer> findAllByAuthor(User user);
}
