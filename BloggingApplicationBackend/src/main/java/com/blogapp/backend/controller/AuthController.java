package com.blogapp.backend.controller;

import com.blogapp.backend.payloads.JwtAuthReq;
import com.blogapp.backend.payloads.JwtAuthResponse;
import com.blogapp.backend.security.JwtTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private JwtTokenHelper jwtTokenHelper;

    @Autowired private UserDetailsService userDetailsService;

    @Autowired private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthReq jwtAuthReq){
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
}
