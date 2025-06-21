package project.streamvaultbackend.entities;

import jakarta.persistence.*;

//commit this changes
import java.util.*;

@Entity
@Table(name = "users")
public class User  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String avatar;

    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "followers")
    private Set<User> following = new HashSet<>();
    public Long getId() {

        return id;
    }
    public String getUsername() {
//Commit this change
        return username;
    }
    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {

        return password;
    }
    public void setPassword(String password) {

        this.password = password;
    }

    public String getAvatar() {

        return avatar;
    }
    public void setAvatar(String avatar) {

        this.avatar = avatar;
    }

    public Set<User> getFollowers() {

        return followers;
    }

    public Set<User> getFollowing() {

        return following;
    }
}
//long id,String username,String avatar,boolean followedByCurrentUser