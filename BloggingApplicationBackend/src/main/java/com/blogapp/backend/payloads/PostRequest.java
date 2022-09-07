package com.blogapp.backend.payloads;

public class PostRequest {

    private String title;

    private String content;

    private String image;

    private String catagoryTitle;

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

    public void setCatagoryTitle(String catagoryTitle) {
        this.catagoryTitle = catagoryTitle;
    }

    public String getCatagoryTitle() {
        return catagoryTitle;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

}
