package project.streamvaultbackend.dtos;

public record CommentDto(long id, String username, String avatar, String text, long timestamp) {}
