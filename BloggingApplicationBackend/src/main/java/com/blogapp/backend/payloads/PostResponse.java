package com.blogapp.backend.payloads;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PostResponse {
    private Integer id;

    private String title;

    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private CategoryDto category;

    private String image;

    private UserResponse author;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setAuthor(UserResponse author) {
        this.author = author;
    }

    public UserResponse getAuthor() {
        return author;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

}
