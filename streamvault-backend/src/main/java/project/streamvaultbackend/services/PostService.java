package project.streamvaultbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.streamvaultbackend.dtos.PostDto;
import project.streamvaultbackend.entities.Post;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.repositories.PostRepository;
import project.streamvaultbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostService {
    @Autowired
   private final PostRepository PostRepository;
    @Autowired
    UserRepository userRepository;

    public PostService(PostRepository postRepository) {
        PostRepository = postRepository;
    }

    public Post createPost(User user, String text) {
        Post post = new Post();
        post.setUser(user);
        post.setText(text);
        post.setTimestamp(System.currentTimeMillis());
        return PostRepository.save(post);
    }

    public List<PostDto> getTimeline(User user, int limit) {
        Set<User> following = new HashSet<>(user.getFollowing());
        following.add(user);
        List<Post> posts = PostRepository.timeline(following, PageRequest.of(0, limit));
        return posts.stream().map(p -> toDto(p, user)).toList();
    }

    public List<PostDto> getUserPosts(Long userId, int limit, User viewer) {
        User postUser = userRepository.findById(userId).orElseThrow();
        List<Post> posts = PostRepository.findByUserOrderByTimestampDesc(postUser, PageRequest.of(0, limit));
        return posts.stream().map(p -> toDto(p, viewer)).toList();
    }

    public void likePost(User user, Long postId) {
        Post post = PostRepository.findById(postId).orElseThrow();
        if (post.getUser().getId().equals(user.getId()))
            throw new RuntimeException("Can't like own post");
        if (post.getLikes().contains(user))
            throw new RuntimeException("Already liked");
        post.getLikes().add(user);
        PostRepository.save(post);
    }

    public PostDto toDto(Post p, User viewer) {
        return new PostDto(
                p.getId(),
                p.getUser().getUsername(),
                p.getUser().getAvatar(),
                p.getText(),
                p.getTimestamp(),
                p.getLikes().size(),
                p.getLikes().contains(viewer)
        );
    }
}