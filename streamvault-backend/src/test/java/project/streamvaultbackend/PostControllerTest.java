package project.streamvaultbackend;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import project.streamvaultbackend.controllers.AuthController;
import project.streamvaultbackend.controllers.PostController;
import project.streamvaultbackend.dtos.*;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.repositories.*;
import project.streamvaultbackend.services.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertThrows;
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class PostControllerTest {
    @Autowired private PostRepository postRepository;
    @Autowired private AuthController authController;
    @Autowired private PostController postController;
    @Autowired private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        // Register two users
        authController.register(new AuthRequest("alice", "pw"));
        authController.register(new AuthRequest("bob", "pw"));
    }

    @Test
    public void createPostAndTimeline() {
        // Alice creates a post
        var createReq = new PostController.CreatePostRequest("alice", "Hello from Alice!");
        PostDto post = postController.createPost(createReq);
        assertEquals("alice", post.username());
        assertEquals("Hello from Alice!", post.text());
        assertEquals(0, post.likeCount());
        assertFalse(post.likedByCurrentUser());

        // Alice's timeline should include the post
        List<PostDto> timeline = postController.timeline(10, "alice");
        assertEquals(1, timeline.size());
        assertEquals(post.text(), timeline.getFirst().text());
    }
    @Test
    public void createPostWithUnknownUserThrows() {
        var req = new PostController.CreatePostRequest("unknown_user", "Should not work");
        assertThrows(Exception.class, () -> postController.createPost(req));
    }

    @Test
    public void userPostsReturnsOnlyUserPosts() {
        // Bob creates two posts
        postController.createPost(new PostController.CreatePostRequest("bob", "Post 1"));
        postController.createPost(new PostController.CreatePostRequest("bob", "Post 2"));
        // Alice creates one post
        postController.createPost(new PostController.CreatePostRequest("alice", "Alice's post"));

        // Get Bob's id using UserService
        User bob = userService.findByUsername("bob");
        Long bobId = bob.getId();

        // Alice requests Bob's posts
        List<PostDto> bobPosts = postController.userPosts("alice", 10, bobId);
        assertEquals(2, bobPosts.size());
        assertTrue(bobPosts.stream().allMatch(p -> p.username().equals("bob")));
    }
//Checks if the LikePost Count is incremented each time a post is liked

    @Test
    public void likePostIncrementsLikeCount() {
        // Alice creates a post
        PostDto post = postController.createPost(new PostController.CreatePostRequest("alice", "Hello from Alice!"));
        Long postId = post.id();
       // Bob follows Alice so he sees her posts in his timeline
        userService.follow((userService.findByUsername("bob")), userService.findByUsername("alice").getId());

        // Bob likes Alice's post
        postController.likePost(postId, new PostController.LikePostRequest("bob"));
        // Alice checks her timeline, likeCount should be 1
        List<PostDto> timeline = postController.timeline(10, "alice");
        System.out.println("Timeline for Alice: " + timeline.size());
        for (PostDto p : timeline) {
            System.out.println(p.text());
        }
        assertFalse(timeline.isEmpty(), "Timeline for Alice should not be empty!");
        assertEquals(1, timeline.getFirst().likeCount());
        assertFalse(timeline.getFirst().likedByCurrentUser());

        // Bob checks his timeline, likedByCurrentUser should be true for that post
        List<PostDto> bobTimeline = postController.timeline(10, "bob");
        assertTrue(bobTimeline.getFirst().likedByCurrentUser());
    }
    @Test
    public void likePostWithUnknownUserThrows() {
        PostDto post = postController.createPost(new PostController.CreatePostRequest("alice", "Like this!"));
        assertThrows(Exception.class, () -> postController.likePost(post.id(), new PostController.LikePostRequest("nonexistent-user")));
    }
    @Test
    public void timelineShowsOnlyOwnPostsIfNoFollowing() {
        postController.createPost(new PostController.CreatePostRequest("alice", "Alice's only post"));
        List<PostDto> timeline = postController.timeline(10, "alice");
        assertEquals(1, timeline.size());
        assertEquals("alice", timeline.getFirst().username());
    }

    @Test
    public void likeNonExistentPostThrows() {
        var req = new PostController.LikePostRequest("bob");
        assertThrows(Exception.class, () -> postController.likePost(123456789L, req));
    }

    @Test
    public void timelineEmptyWhenNoPosts() {
        List<PostDto> timeline = postController.timeline(10, "alice");
        assertTrue(timeline.isEmpty(), "Timeline should be empty when there are no posts");
    }
    @Test
    public void cannotLikeOwnPost() {
        // Alice creates a post
        PostDto post = postController.createPost(new PostController.CreatePostRequest("alice", "My own post"));
        Long postId = post.id();

        // Alice tries to like her own post (should throw)
        assertThrows(RuntimeException.class, () -> postController.likePost(postId, new PostController.LikePostRequest("alice")));
    }
    @Transactional
    @Test
    public void unlikePostRemovesLike() {
        // Alice creates a post
        PostDto post = postController.createPost(new PostController.CreatePostRequest("alice", "Post for unlike test"));
        Long postId = post.id();
        // Bob follows Alice
        userService.follow(userService.findByUsername("bob"), userService.findByUsername("alice").getId());

        // Bob likes the post
        postController.likePost(postId, new PostController.LikePostRequest("bob"));
        // Bob unlikes the post (assume you implemented postController.unlikePost)
        postController.unlikePost(postId, new PostController.LikePostRequest("bob"));

        // Timeline for Bob: likeCount is 0, likedByCurrentUser is false
        List<PostDto> timeline = postController.timeline(10, "bob");
        assertEquals(1, timeline.size());
        assertEquals(0, timeline.getFirst().likeCount());
        assertFalse(timeline.getFirst().likedByCurrentUser());
    }

    @Test
    public void cannotUnlikeIfNotLiked() {
        // Alice creates a post
        PostDto post = postController.createPost(new PostController.CreatePostRequest("alice", "Should not be able to unlike"));
        Long postId = post.id();
        // Bob tries to unlike without liking first (should throw)
        assertThrows(RuntimeException.class, () -> postController.unlikePost(postId, new PostController.LikePostRequest("bob")));
    }

    @Test
    public void cannotLikeTwice() {
        // Bob creates a post
        PostDto post = postController.createPost(new PostController.CreatePostRequest("bob", "Bob's post"));
        Long postId = post.id();

        // Alice likes Bob's post
        postController.likePost(postId, new PostController.LikePostRequest("alice"));
        // Alice tries to like again
        assertThrows(RuntimeException.class, () -> postController.likePost(postId, new PostController.LikePostRequest("alice")));
    }
    @Test
    public void timelineRespectsLimit() {
        for (int i = 0; i < 15; i++) {
            postController.createPost(new PostController.CreatePostRequest("alice", "Post #" + i));
        }
        List<PostDto> timeline = postController.timeline(10, "alice");
        assertEquals(10, timeline.size());
    }

    @Test
    public void timelineWithUnknownUserThrows() {
        assertThrows(Exception.class, () -> postController.timeline(20, "unknown"));
    }

    @Test
    public void userPostsWithUnknownUserThrows() {
        // Use a bogus userId (e.g., 9999)
        assertThrows(Exception.class, () -> postController.userPosts("alice", 10, 9999L));
    }
    @Test
    public void deleteOwnPostRemovesFromTimeline() {
        // Alice creates a post
        PostDto post = postController.createPost(new PostController.CreatePostRequest("alice", "To be deleted"));
        Long postId = post.id();
        // Alice deletes her own post
        postController.deletePost(postId, "alice");

        // Timeline for Alice should be empty
        List<PostDto> timeline = postController.timeline(10, "alice");
        assertTrue(timeline.isEmpty(), "Timeline should be empty after deleting the only post");
    }

    @Test
    public void cannotDeleteOthersPost() {
        // Alice creates a post
        PostDto post = postController.createPost(new PostController.CreatePostRequest("alice", "Can't be deleted by Bob"));
        Long postId = post.id();

        // Bob tries to delete Alice's post (should throw)
        assertThrows(RuntimeException.class, () -> postController.deletePost(postId, "bob"));
    }


}