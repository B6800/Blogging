package project.streamvaultbackend.services;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.streamvaultbackend.entities.User;
import project.streamvaultbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;



@Service
public class AuthService {

    public final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
@Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent())
            throw new RuntimeException("Username already exists");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be null or empty");
        if (userRepository.findByUsername(username).isPresent())
            throw new RuntimeException("Username already exists");
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setAvatar("https://api.dicebear.com/7.x/thumbs/svg?seed=" + username);
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty())
            throw new RuntimeException("User not found");
        if ( !passwordEncoder.matches(password, userOpt.get().getPassword()))
            throw new RuntimeException("Wrong password");
        return userOpt.get();
    }

}