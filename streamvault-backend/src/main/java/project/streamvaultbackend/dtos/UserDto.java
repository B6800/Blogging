package project.streamvaultbackend.dtos;

public record UserDto (long id,String username,String avatar,boolean followedByCurrentUser){}

// id,Username and Avatar