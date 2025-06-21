package project.streamvaultbackend.controllers;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;
import project.streamvaultbackend.dtos.PostDto;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.services.PostService;
import project.streamvaultbackend.services.UserService;


import java.util.List;
@CrossOrigin(origins = "*")
@Transactional
@RestController
public class PostController {
    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }
    public record CreatePostRequest(String username, String text) {}
    @PostMapping("/api/posts")
    public PostDto createPost(@RequestBody CreatePostRequest req) {//commit
        User user = userService.findByUsername(req.username());
        return postService.toDto(postService.createPost(user, req.text()), user);
    }

    @GetMapping("/api/posts/timeline")//commit
    public List<PostDto> timeline(@RequestParam(defaultValue = "20") int limit, @RequestParam String username ) {
        User user = userService.findByUsername(username);
        return postService.getTimeline(user, limit);
    }

    @GetMapping("/api/posts/user/{userId}/posts")//commit
    public List<PostDto> userPosts(@RequestParam String username, @RequestParam(defaultValue = "20") int limit,@PathVariable Long userId) {
        User viewer = userService.findByUsername(username);
        return postService.getUserPosts(userId, limit, viewer);
    }
    public record LikePostRequest(String username) {}//commit
    @PostMapping("/api/posts/{postId}/like")
    public void likePost(@PathVariable Long postId,@RequestBody LikePostRequest req) {
        User user = userService.findByUsername(req.username());
        postService.likePost(user, postId);

    }
}
