package project.streamvaultbackend.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.streamvaultbackend.dtos.CommentDto;
import project.streamvaultbackend.entities.Comment;
import project.streamvaultbackend.entities.Post;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.repositories.CommentRepository;
import project.streamvaultbackend.repositories.PostRepository;
import project.streamvaultbackend.repositories.UserRepository;

import java.util.List;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentDto addComment(Long postId, String username, String text) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setText(text);
        comment.setTimestamp(System.currentTimeMillis());
        comment = commentRepository.save(comment);
        return toDto(comment);
    }

    public List<CommentDto> getComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return commentRepository.findByPostOrderByTimestampAsc(post).stream()
                .map(this::toDto)
                .toList();
    }

    public CommentDto toDto(Comment c) {
        return new CommentDto(
                c.getId(),
                c.getUser().getUsername(),
                c.getUser().getAvatar(),
                c.getText(),
                c.getTimestamp()
        );
    }
}
