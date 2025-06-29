package project.streamvaultbackend.controllers;

import org.springframework.web.bind.annotation.*;
import project.streamvaultbackend.dtos.CommentDto;
import project.streamvaultbackend.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@CrossOrigin(origins = "*")
public class CommentController {
    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    public record AddCommentRequest(String username, String text) {}

    @PostMapping
    public CommentDto addComment(@PathVariable Long postId, @RequestBody AddCommentRequest req) {
        return commentService.addComment(postId, req.username(), req.text());
    }

    @GetMapping
    public List<CommentDto> getComments(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }
}
