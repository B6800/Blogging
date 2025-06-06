package project.streamvaultbackend.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.streamvaultbackend.dtos.AuthRequest;
import project.streamvaultbackend.dtos.UserDto;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
     AuthService authService;

    @PostMapping("/register")
    public UserDto register(@RequestBody AuthRequest req) {
        User user = authService.register(req.username(), req.password());
        return new UserDto(user.getId(), user.getUsername(), user.getAvatar(), false);
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody AuthRequest req) {
        User user = authService.login(req.username(), req.password());
        return new UserDto(user.getId(), user.getUsername(), user.getAvatar(), false);
    }
}
