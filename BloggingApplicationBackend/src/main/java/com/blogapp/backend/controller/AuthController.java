package com.blogapp.backend.controller;

import com.blogapp.backend.exception.MethodArgumentsNotFound;
import com.blogapp.backend.payloads.JwtAuthReq;
import com.blogapp.backend.payloads.JwtAuthResponse;
import com.blogapp.backend.payloads.UserRequest;
import com.blogapp.backend.payloads.UserResponse;
import com.blogapp.backend.security.JwtTokenHelper;
import com.blogapp.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private JwtTokenHelper jwtTokenHelper;

    @Autowired private UserDetailsService userDetailsService;

    @Autowired private AuthenticationManager authenticationManager;

    @Autowired private UserService userService;

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@Valid @RequestBody JwtAuthReq jwtAuthReq){
        this.authenticate(jwtAuthReq.getEmail(),jwtAuthReq.getPassword());
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthReq.getEmail());

            String token = this.jwtTokenHelper.generateToken(userDetails);

            JwtAuthResponse response = new JwtAuthResponse();

            response.setToken(token);

            return new ResponseEntity<>(response, HttpStatus.OK);

    }

    private void authenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,password);
            this.authenticationManager.authenticate(authenticationToken);

    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest userRequest){
        if(userRequest == null){
            throw new MethodArgumentsNotFound("User Request is null");
        }
        UserResponse res = this.userService.registerUser(userRequest);
            return new ResponseEntity<>(res,HttpStatus.CREATED);

    }



}
