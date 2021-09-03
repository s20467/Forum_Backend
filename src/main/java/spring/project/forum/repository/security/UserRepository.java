package spring.project.forum.repository.security;

import org.springframework.data.repository.CrudRepository;
import spring.project.forum.model.security.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
