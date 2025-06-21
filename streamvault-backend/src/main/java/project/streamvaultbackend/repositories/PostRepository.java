package project.streamvaultbackend.repositories;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import project.streamvaultbackend.entities.Post;
import project.streamvaultbackend.entities.User;

import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserOrderByTimestampDesc(User user, PageRequest pageable);
    List<Post> findByUserIdInOrderByTimestampDesc(Set<Long> userIds, PageRequest pageable);
}
