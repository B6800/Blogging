package project.streamvaultbackend.controllers;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.streamvaultbackend.dtos.AuthRequest;
import project.streamvaultbackend.dtos.UserDto;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.services.AuthService;
@CrossOrigin(origins = "*")
@RestController
@Transactional
public class AuthController {
     public final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {

        this.authService = authService;
    }

    @PostMapping("/api/auth/register")
    public UserDto register(@RequestBody AuthRequest req) {
        User user = authService.register(req.username(), req.password());
        return new UserDto(user.getId(), user.getUsername(), user.getAvatar(), false);
    }

    @PostMapping("/api/auth/login")
    public UserDto login(@RequestBody AuthRequest req) {
        User user = authService.login(req.username(), req.password());
        return new UserDto(user.getId(), user.getUsername(), user.getAvatar(), false);
    }
}
