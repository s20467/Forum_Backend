package spring.project.forum.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.project.forum.model.security.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
