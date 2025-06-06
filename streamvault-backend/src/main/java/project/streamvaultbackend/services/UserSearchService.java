package project.streamvaultbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.streamvaultbackend.dtos.UserDto;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.repositories.UserRepository;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private final UserRepository UserRepository;

    public UserService(UserRepository userRepository) {
        UserRepository = userRepository;
    }

    public List<UserDto> searchUsers(String search, User currentUser) {
        List<User> found = UserRepository.findByUsernameContainingIgnoreCase(search);
        return found.stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .map(u -> new UserDto(
                        u.getId(),
                        u.getUsername(),
                        u.getAvatar(),
                        currentUser.getFollowing().contains(u)
                )).toList();
    }

    public void follow(User currentUser, Long targetId) {
        User target = UserRepository.findById(targetId).orElseThrow();
        currentUser.getFollowing().add(target);
        target.getFollowers().add(currentUser);
        UserRepository.save(currentUser);
        UserRepository.save(target);
    }
    public void unfollow(User currentUser, Long targetId) {
        User target = UserRepository.findById(targetId).orElseThrow();
        currentUser.getFollowing().remove(target);
        target.getFollowers().remove(currentUser);
        UserRepository.save(currentUser);
        UserRepository.save(target);
    }

    public List<UserDto> searchUsers(String username, User currentUser) {
    }
}