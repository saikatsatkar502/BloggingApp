package com.blogapp.backend.payloads;

import com.blogapp.backend.model.Role;

import javax.validation.constraints.*;

public class UserRequest {
    @NotEmpty(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email is invalid")
    private String email;

    @NotEmpty(message = "Name is required")
    @Size(min = 4, message = "UserName must be 4 char long.")
    private String name;

    @NotEmpty(message = "Password is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters long, contain at least one digit, one uppercase letter, one lowercase letter and one special character")
    private String password;
    @NotNull(message = "Role required")
    private Role role;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public UserRequest() {
        super();
    }
}
