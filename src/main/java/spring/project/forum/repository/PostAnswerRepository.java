package spring.project.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.project.forum.model.PostAnswer;

public interface PostAnswerRepository extends JpaRepository<PostAnswer, Integer> {
}
