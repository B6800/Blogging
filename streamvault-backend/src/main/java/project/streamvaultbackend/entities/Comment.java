package project.streamvaultbackend.entities;

import jakarta.persistence.*;


@Entity
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    private Post post;

    @Column(nullable = false, length = 280)
    private String text;

    private long timestamp;

    // Getters & setters
    public Long getId() {
        return id;
    }
    @SuppressWarnings("unused")
    public Post getPost() {
        return post;
    }
    public void setPost(Post post) {
        this.post=post;
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
