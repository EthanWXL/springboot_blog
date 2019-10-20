package hello.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;

public class User {
    private Integer id;
    private String username;
    @JsonIgnore
    private String encodedPassword;
    private String avatar;
    private Instant createdAt;
    private Instant updatedAt;

    public User(Integer id, String username, String encodedPassword) {
        this.id = id;
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.avatar = "";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }


}
