package spring.project.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.project.forum.model.PostQuestion;

public interface PostQuestionRepository extends JpaRepository<PostQuestion, Integer> {
}
