package com.blogapp.backend.payloads;

public class CommentsResponse {
    private Integer id;
    private String content;

    private PostResponse post;

    public void setPost(PostResponse post) {
        this.post = post;
    }

    public PostResponse getPost() {
        return post;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
