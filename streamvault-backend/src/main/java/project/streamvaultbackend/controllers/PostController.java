package project.streamvaultbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.streamvaultbackend.dtos.PostDto;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.services.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    PostService postService;

    private User getCurrentUser() {

        return authService.userRepository.findAll().get(0);
    }

    @PostMapping
    public PostDto createPost(@RequestBody String text) {
        return postService.toDto(postService.createPost(getCurrentUser(), text), getCurrentUser());
    }

    @GetMapping("/timeline")
    public List<PostDto> timeline(@RequestParam(defaultValue = "20") int limit) {
        return postService.getTimeline(getCurrentUser(), limit);
    }

    @GetMapping("/user/{userId}")
    public List<PostDto> userPosts(@PathVariable Long userId, @RequestParam(defaultValue = "20") int limit) {
        return postService.getUserPosts(userId, limit, getCurrentUser());
    }

    @PostMapping("/{postId}/like")
    public void likePost(@PathVariable Long postId) {
        postService.likePost(getCurrentUser(), postId);
    }
}
//Add getters and setters