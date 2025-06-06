package project.streamvaultbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.streamvaultbackend.dtos.UserDto;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.services.AuthService;
import project.streamvaultbackend.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;

    private User getCurrentUser() {

        return authService.userRepository.findAll().get(0);
    }

    @GetMapping("/search")
    public List<UserDto> searchUsers(@RequestParam String username) {
        return userService.searchUsers(username, getCurrentUser());
    }

    @PostMapping("/follow/{id}")
    public void follow(@PathVariable Long id) {
        userService.follow(getCurrentUser(), id);
    }

    @PostMapping("/unfollow/{id}")
    public void unfollow(@PathVariable Long id) {
        userService.unfollow(getCurrentUser(), id);
    }
}
//Add getters and setters