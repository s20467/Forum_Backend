package spring.project.forum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.project.forum.model.PostAnswer;
import spring.project.forum.model.PostQuestion;
import spring.project.forum.model.security.User;


import java.util.List;

public interface PostQuestionRepository extends JpaRepository<PostQuestion, Integer> {
    Page<PostQuestion> findAllByAuthor(Pageable pageable, User user);
    List<PostQuestion> findAllByAuthor(User user);
    List<PostQuestion> findAllByBestAnswerIsNull();
    Page<PostQuestion> findAllByBestAnswerIsNull(Pageable pageable);

}
