package project.streamvaultbackend;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import project.streamvaultbackend.controllers.AuthController;
import project.streamvaultbackend.controllers.CommentController;
import project.streamvaultbackend.controllers.PostController;
import project.streamvaultbackend.dtos.*;
import project.streamvaultbackend.repositories.CommentRepository;
import project.streamvaultbackend.repositories.PostRepository;
import project.streamvaultbackend.repositories.UserRepository;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class CommentControllerTest {
    @Autowired private AuthController authController;
    @Autowired private PostController postController;
    @Autowired private CommentController commentController;
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentRepository commentRepository;

    @BeforeEach
    public void setup() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
        authController.register(new AuthRequest("alice", "pw"));
        authController.register(new AuthRequest("bob", "pw"));
    }

    @Test
    public void testAddAndListComments() {
        // Alice creates a post
        var postDto = postController.createPost(new PostController.CreatePostRequest("alice", "Post with comments"));

        // Bob comments on Alice's post
        var commentReq = new CommentController.AddCommentRequest("bob", "Nice post!");
        CommentDto commentDto = commentController.addComment(postDto.id(), commentReq);
        assertEquals("bob", commentDto.username());
        assertEquals("Nice post!", commentDto.text());

        // List comments for that post
        List<CommentDto> comments = commentController.getComments(postDto.id());
        assertEquals(1, comments.size());
        assertEquals("Nice post!", comments.getFirst().text());
    }

    @Test
    public void testAddCommentWithUnknownUserThrows() {
        var postDto = postController.createPost(new PostController.CreatePostRequest("alice", "Another post"));
        var commentReq = new CommentController.AddCommentRequest("unknownuser", "Hello!");
        assertThrows(Exception.class, () -> commentController.addComment(postDto.id(), commentReq));
    }

    @Test
    public void testAddCommentToNonexistentPostThrows() {
        var commentReq = new CommentController.AddCommentRequest("alice", "Hi there");
        assertThrows(Exception.class, () -> commentController.addComment(999L, commentReq));
    }

    @Test
    public void testGetCommentsForPostWithNoCommentsReturnsEmptyList() {
        var postDto = postController.createPost(new PostController.CreatePostRequest("alice", "No comments yet"));
        List<CommentDto> comments = commentController.getComments(postDto.id());
        assertTrue(comments.isEmpty());
    }
}