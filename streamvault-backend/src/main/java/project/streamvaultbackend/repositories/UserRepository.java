package project.streamvaultbackend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import project.streamvaultbackend.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
   List<User> findAllByUsernameContainingIgnoreCase(String name);
   Optional<User> findByUsername(String username);
}
