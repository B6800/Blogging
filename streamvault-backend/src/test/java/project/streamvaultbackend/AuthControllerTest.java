package project.streamvaultbackend;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import project.streamvaultbackend.controllers.AuthController;
import project.streamvaultbackend.dtos.*;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.repositories.UserRepository;
import project.streamvaultbackend.services.AuthService;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class AuthControllerTest {
   @Autowired private AuthController authController;
    @Autowired private UserRepository userRepository;
    @Autowired private AuthService authService;
    @BeforeEach
    public void setup() {
        // Setup can register a baseline user if needed
        authController.register(new AuthRequest("existingUser", "password"));
    }

    @Test
    public void registerSuccessAndLogin() {
        AuthRequest req = new AuthRequest("bob", "pw");
        UserDto registered = authController.register(req);
        assertEquals("bob", registered.username());

        UserDto loggedIn = authController.login(req);
        assertEquals(registered.username(), loggedIn.username());
    }
    @Test
    void registerThrowsIfUsernameAlreadyExists() {
        String username = "duplicate";
        String password = "pw";
        // First registration should succeed
        authService.register(username, password);
        // Second registration with same username should throw
        Exception ex = assertThrows(RuntimeException.class, () -> authService.register(username, password));
        assertEquals("Username already exists", ex.getMessage());
    }
    @Test
    public void registerExistingUserThrows() {
        AuthRequest req = new AuthRequest("existingUser", "password");
        assertThrows(Exception.class, () -> authController.register(req));
    }
    @Test
    public void registerWithNullOrEmptyPasswordThrows() {
        assertThrows(Exception.class, () -> authController.register(new AuthRequest("user1", null)));
        assertThrows(Exception.class, () -> authController.register(new AuthRequest("user2", "")));
    }
    @Test
    public void loginWithWrongPasswordFails() {
        AuthRequest req = new AuthRequest("bob", "pw");
        authController.register(req);
        AuthRequest wrongPw = new AuthRequest("bob", "wrong");
        assertThrows(Exception.class, () -> authController.login(wrongPw));
    }
    @Test
    public void loginNonExistingUserThrows() {
        AuthRequest req = new AuthRequest("notfound", "pw");
        assertThrows(Exception.class, () -> authController.login(req));
    }
    @Test
    public void loginSucceedsAfterRegister() {
        AuthRequest req = new AuthRequest("tester123", "test123");
        UserDto registered = authController.register(req);
        assertEquals("tester123", registered.username());
        UserDto loggedIn = authController.login(req);
        assertEquals(registered.username(), loggedIn.username());

    }

    @Test
    public void passwordIsHashedWhenRegistered() {
        AuthRequest req = new AuthRequest("hashUser", "plainPassword");
        authController.register(req);
        User user = userRepository.findByUsername("hashUser").orElseThrow();
        // The stored password should NOT be the plain text password
        assertNotEquals("plainPassword", user.getPassword());
        // Should start with BCrypt prefix
        assertTrue(user.getPassword().startsWith("$2"));
        System.out.println("Stored password: " + user.getPassword());

    }


}

