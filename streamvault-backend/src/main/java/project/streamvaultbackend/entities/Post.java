package project.streamvaultbackend.entities;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false, length = 280)
    private String text;

    private long timestamp;

@ManyToMany
    private Set<User> likes = new HashSet<>();


    public Long getId() {

        return id;
    }

    public User getUser() {

        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {

        return text;
    }
    public void setText(String text) {

        this.text = text;
    }

    public long getTimestamp() {

        return timestamp;
    }
    public void setTimestamp(long timestamp) {

        this.timestamp = timestamp;// Commit this change
    }

    public Set<User> getLikes() {

        return likes;
    }
}