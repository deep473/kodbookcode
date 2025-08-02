package com.kodbook.entities;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;
    private int likes;

    @ElementCollection
    private List<String> comments;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;

    @Column(length = 2048)
    private String videoUrl;

    public enum PostType {
        IMAGE, VIDEO
    }

    public Post() {}

    public Post(Long id, String caption, int likes, List<String> comments,
                User user, PostType postType, byte[] photo, String videoUrl) {
        this.id = id;
        this.caption = caption;
        this.likes = likes;
        this.comments = comments;
        this.user = user;
        this.postType = postType;
        this.photo = photo;
        this.videoUrl = videoUrl;
    }

    // Convenience: encode photo for display
    public String getPhotoBase64() {
        if (photo == null) return null;
        return Base64.getEncoder().encodeToString(photo);
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public List<String> getComments() { return comments; }
    public void setComments(List<String> comments) { this.comments = comments; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public PostType getPostType() { return postType; }
    public void setPostType(PostType postType) { this.postType = postType; }

    public byte[] getPhoto() { return photo; }
    public void setPhoto(byte[] photo) { this.photo = photo; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    @Override
    public String toString() {
        return "Post [id=" + id +
               ", caption=" + caption +
               ", likes=" + likes +
               ", comments=" + comments +
               ", user=" + user +
               ", postType=" + postType +
               ", photo=" + Arrays.toString(photo) +
               ", videoUrl=" + videoUrl +
               "]";
    }
}
