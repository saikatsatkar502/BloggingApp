package com.blogapp.backend.controller;

import com.blogapp.backend.exception.MethodArgumentsNotFound;
import com.blogapp.backend.exception.ResourceNotFoundException;
import com.blogapp.backend.model.User;
import com.blogapp.backend.payloads.UserRequest;
import com.blogapp.backend.payloads.UserResponse;
import com.blogapp.backend.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-all")
    public ResponseEntity<List<UserResponse>> findAll() throws ResourceNotFoundException {
        List<UserResponse> users = userService.findAll();
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        }
        throw new ResourceNotFoundException("User List is empty ");


    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {

        if (id != 0) {
            User user = userService.findById(id);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                throw new ResourceNotFoundException("User", "id", id);
            }
        } else {
            throw new MethodArgumentsNotFound("User Id is Not Given to search");
        }

    }

    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {

        if (email.isEmpty()) {
            throw new MethodArgumentsNotFound("User email is Not Given to search");

        }
        User user = userService.findByEmail(email);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);

        } else {
            throw new ResourceNotFoundException("User Data Not Found With email :" + email);
        }

    }

    @PostMapping("/save")
    public ResponseEntity<UserResponse> saveUser(@Valid @RequestBody UserRequest user) {
        if (user != null) {
            UserResponse userResponse = userService.save(user);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            throw new MethodArgumentsNotFound("User Data is Not Given to save");
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable int id, @Valid @RequestBody UserRequest user) {
        if (user != null && id != 0) {
            UserResponse userResponse = userService.update(user, id);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            throw new MethodArgumentsNotFound("User ","update",user);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable int id) {
        if (id != 0) {
            UserResponse userResponse = userService.delete(id);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            throw new MethodArgumentsNotFound("Id","delete user",id);
        }

    }
    
    @DeleteMapping("/delete-by-email/{email}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable String email) {
        if (email.isEmpty()) {
            UserResponse userResponse = userService.deleteByEmail(email);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            throw new MethodArgumentsNotFound("Email","delete user",email);
        }

    }


}