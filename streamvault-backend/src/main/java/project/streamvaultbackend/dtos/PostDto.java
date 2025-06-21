package project.streamvaultbackend.dtos;

public record PostDto(long id,String username,String avatar,String text,long timestamp,int likeCount,boolean likedByCurrentUser) {
}
