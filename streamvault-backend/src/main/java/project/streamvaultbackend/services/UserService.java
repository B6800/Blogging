package project.streamvaultbackend.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.streamvaultbackend.dtos.UserDto;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.repositories.UserRepository;

import java.util.*;
@Transactional
@Service
public class UserService {
    private final UserRepository UserRepository;
    @Autowired
    public UserService(UserRepository userRepository) {

        UserRepository = userRepository;
    }

    public User findByUsername(String username) {
        return UserRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + username));
    }

    public List<UserDto> searchUsers(String search, String username) {
        User currentUser = findByUsername(username);
        List<User> found = UserRepository.findAllByUsernameContainingIgnoreCase(search);
        return found.stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .map(u -> new UserDto(
                        u.getId(),
                        u.getUsername(),
                        u.getAvatar(),
                        currentUser.getFollowing().contains(u)
                )).toList();
    }

public void follow(User currentUser , Long targetId) {
    if (currentUser.getId().equals(targetId)) {
        throw new RuntimeException("Cannot follow yourself");
    }
    User managedCurrentUser = UserRepository.findById(currentUser.getId())
            .orElseThrow(() -> new NoSuchElementException("User not found: " + currentUser.getId()));
    User target = UserRepository.findById(targetId)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + targetId));
    currentUser.getFollowing().add(target);
    managedCurrentUser.getFollowing().add(target);
    target.getFollowers().add(currentUser);
    UserRepository.save(currentUser);
    UserRepository.save(target);
}


    public void unfollow(User currentUser, Long targetId) {
        User managedCurrentUser = UserRepository.findById(currentUser.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found: " + currentUser.getId()));
        User target = UserRepository.findById(targetId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + targetId));
        managedCurrentUser.getFollowing().remove(target);
        target.getFollowers().remove(managedCurrentUser);
        UserRepository.save(managedCurrentUser);
        UserRepository.save(target);
    }
    public User findById(Long id) {
        return UserRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
    }

}