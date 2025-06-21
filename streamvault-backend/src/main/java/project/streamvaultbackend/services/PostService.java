package project.streamvaultbackend.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.streamvaultbackend.dtos.PostDto;
import project.streamvaultbackend.entities.Post;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.repositories.PostRepository;
import project.streamvaultbackend.repositories.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class PostService {
   private final PostRepository postRepository;//commit this change
    private final UserRepository userRepository;
    @Autowired
    public PostService(PostRepository postRepository,UserRepository userRepository) {

        this. postRepository = postRepository;
        this.userRepository=userRepository;
    }

    public Post createPost(User user, String text) {
        Post post = new Post();
        post.setUser(user);
        post.setText(text);
        post.setTimestamp(System.currentTimeMillis());
        return postRepository.save(post);
    }

    public List<PostDto> getTimeline(User user, int limit) {//commit this
        Set<Long> userIds = user.getFollowing().stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        userIds.add(user.getId());

        List<Post> posts = postRepository.findByUserIdInOrderByTimestampDesc(userIds, PageRequest.of(0, limit));
        return posts.stream().map(p -> toDto(p, user)).toList();
    }

    public List<PostDto> getUserPosts(long userId, int limit, User viewer) {
        User postUser = userRepository.findById(userId).orElseThrow();
        List<Post> posts = postRepository.findByUserOrderByTimestampDesc(postUser, PageRequest.of(0, limit));
        return posts.stream().map(p -> toDto(p, viewer)).toList();
    }

    public void likePost(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        if (post.getUser().getId().equals(user.getId()))
            throw new RuntimeException("Can't like own post");
        if (post.getLikes().contains(user))
            throw new RuntimeException("Already liked");
        post.getLikes().add(user);
        postRepository.save(post);

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