package project.streamvaultbackend.controllers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.streamvaultbackend.dtos.UserDto;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.services.UserService;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@Transactional
@RequestMapping("/api/users")
public class UserController {

   private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")//commit
    public List<UserDto> searchUsers(@RequestParam String username,@RequestParam String query) {
        return userService.searchUsers(query, username);
    }
    @PostMapping("/{id}/follow")
    public void follow(@PathVariable Long id,@RequestParam String username) {
        User currentUser = userService.findByUsername(username);
        userService.follow(currentUser, id);
    }

    @PostMapping("/{id}/unfollow")
    public void unfollow(@PathVariable Long id,@RequestParam String username) {
        User currentUser = userService.findByUsername(username);
        userService.unfollow(currentUser, id);
    }
    @Transactional
    @GetMapping("/{id}/followers")
    public List<UserDto> getFollowers(@PathVariable Long id) {
        User user = userService.findById(id);
        return user.getFollowers().stream()
                .map(follower -> new UserDto(
                        follower.getId(),
                        follower.getUsername(),
                        follower.getAvatar(),
                        false
                ))
                .toList();
    }

}
