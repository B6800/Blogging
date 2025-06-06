package project.streamvaultbackend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import project.streamvaultbackend.entities.User;
public interface UserRepository extends JpaRepository<User,Long> {
   boolean findByUsernameContainingIgnoreCase(String name);
}
