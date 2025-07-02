package project.streamvaultbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.streamvaultbackend.entities.Comment;
import project.streamvaultbackend.entities.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByTimestampAsc(Post post);
}
