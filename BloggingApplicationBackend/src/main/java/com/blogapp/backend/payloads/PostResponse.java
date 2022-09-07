package com.blogapp.backend.payloads;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PostResponse {
    private Integer id;

    private String title;

    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private CatagoryDto catagory;

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

    public void setCatagory(CatagoryDto catagory) {
        this.catagory = catagory;
    }

    public CatagoryDto getCatagory() {
        return catagory;
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
