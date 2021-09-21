package spring.project.forum.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.project.forum.model.security.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}
