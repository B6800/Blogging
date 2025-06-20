package project.streamvaultbackend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import project.streamvaultbackend.controllers.AuthController;
import project.streamvaultbackend.controllers.UserController;
import project.streamvaultbackend.dtos.AuthRequest;
import project.streamvaultbackend.dtos.UserDto;
import project.streamvaultbackend.repositories.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertThrows;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired private UserController userController;
     @Autowired private AuthController authController;
@Autowired private UserRepository userRepository;
      private long danaId;
    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        UserDto dana= authController.register(new AuthRequest("dana","pw"));
        danaId=dana.id();
        authController.register(new AuthRequest("carl", "pw"));
        authController.register(new AuthRequest("alice", "pw"));


    }

    @Test
    public void searchUsersFindsOtherUsers() {
        // alice searches for bob
        List<UserDto> result = userController.searchUsers("carl", "dana");
        assertEquals(1, result.size());
        assertEquals("dana", result.getFirst().username());
        assertFalse(result.getFirst().followedByCurrentUser());
    }

    @Test
    public void followAndUnfollowUser() {
        // alice follows bob
        userController.follow(danaId, "carl");
        List<UserDto> result = userController.searchUsers("carl", "dana");
        assertTrue(result.getFirst().followedByCurrentUser());

        // Check followers
        List<UserDto> followers = userController.getFollowers(danaId);
        assertEquals(1, followers.size());
        assertEquals("carl", followers.getFirst().username());

        // carl unfollows dana
        userController.unfollow(danaId, "carl");
        result = userController.searchUsers("carl", "dana");
        assertFalse(result.getFirst().followedByCurrentUser());

        followers = userController.getFollowers(danaId);
        assertTrue(followers.isEmpty());
    }

    @Test
    public void getFollowersWhenNoFollowers() {
        List<UserDto> followers = userController.getFollowers(danaId);
        assertTrue(followers.isEmpty());
    }

    @Test
    public void followNonExistentUserThrows() {
        assertThrows(Exception.class, () -> userController.follow(99999L, "carl"));
    }

    @Test
    public void unfollowNonExistentUserThrows() {
        assertThrows(Exception.class, () -> userController.unfollow(99999L, "carl"));
    }

    @Test
    public void searchReturnsEmptyIfNoMatch() {
        List<UserDto> result = userController.searchUsers("carl", "doesnotexist");
        assertTrue(result.isEmpty());
    }

    @Test
    public void searchCannotFindYourself() {
        List<UserDto> result = userController.searchUsers("alice", "alice");
        assertTrue(result.isEmpty());
    }
}