package spring.project.forum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.project.forum.model.Question;
import spring.project.forum.model.security.User;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Page<Question> findAllByAuthor(Pageable pageable, User user);

    List<Question> findAllByAuthor(User user);

    List<Question> findAllByBestAnswerIsNull();

    Page<Question> findAllByBestAnswerIsNull(Pageable pageable);

}
