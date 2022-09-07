package com.blogapp.backend.payloads;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CatagoryDto {

    private Integer id;
    @NotEmpty(message = "Title is required")
    @Size(min = 2, message = "Title must be atleast 2 characters")
    private String title;

    private String description;

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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
