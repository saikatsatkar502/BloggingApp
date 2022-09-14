package com.blogapp.backend.payloads;


public class JwtAuthResponse {

    private String token;

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

}
