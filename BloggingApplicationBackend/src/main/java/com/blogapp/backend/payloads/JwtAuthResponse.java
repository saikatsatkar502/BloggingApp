package com.blogapp.backend.payloads;


public class JwtAuthResponse {

    private String token;

    private UserResponse user;

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    public void setUser(UserResponse user){
        this.user = user;
    }
    public UserResponse getUser(){
        return user;
    }

}
