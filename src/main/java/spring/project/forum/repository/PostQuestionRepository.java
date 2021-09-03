package spring.project.forum.repository;

import org.springframework.data.repository.CrudRepository;
import spring.project.forum.model.PostQuestion;

public interface PostQuestionRepository extends CrudRepository<PostQuestion, Integer> {
}
