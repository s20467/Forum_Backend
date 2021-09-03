package spring.project.forum.repository;

import org.springframework.data.repository.CrudRepository;
import spring.project.forum.model.PostAnswer;

public interface PostAnswerRepository extends CrudRepository<PostAnswer, Integer> {
}
