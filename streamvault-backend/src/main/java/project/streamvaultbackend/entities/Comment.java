package project.streamvaultbackend.entities;

import jakarta.persistence.*;


@Entity
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false, length = 280)
    private String text;

    private long timestamp;

    // Getters & setters
    public Long getId() {
        return id;
    }

    public void setPost() {
    }
    public User getUser() {
        return user; }
    public void setUser(User user) {
        this.user = user;
    }
    public String getText() {
        return text; }
    public void setText(String text) {
        this.text = text;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
