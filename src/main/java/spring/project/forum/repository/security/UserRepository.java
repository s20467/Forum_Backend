package spring.project.forum.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.project.forum.model.security.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

}
