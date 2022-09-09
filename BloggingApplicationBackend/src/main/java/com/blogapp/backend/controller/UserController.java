package com.blogapp.backend.controller;

import com.blogapp.backend.config.AppConfiguration;
import com.blogapp.backend.exception.MethodArgumentsNotFound;
import com.blogapp.backend.exception.ResourceNotFoundException;
import com.blogapp.backend.model.User;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.payloads.UserRequest;
import com.blogapp.backend.payloads.UserResponse;
import com.blogapp.backend.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.el.MethodNotFoundException;
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
            throw new MethodArgumentsNotFound("User ", "update", user);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable int id) {
        if (id != 0) {
            UserResponse userResponse = userService.delete(id);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            throw new MethodArgumentsNotFound("Id", "delete user", id);
        }

    }

    @DeleteMapping("/delete-by-email/{email}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable String email) {
        if (email.isEmpty()) {
            UserResponse userResponse = userService.deleteByEmail(email);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            throw new MethodArgumentsNotFound("Email", "delete user", email);
        }

    }

    @GetMapping("/get-all-by-page")
    public ResponseEntity<PaginationApiResponse> findAllByPage(
            @RequestParam(defaultValue = AppConfiguration.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = AppConfiguration.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "sortBy", defaultValue = AppConfiguration.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = AppConfiguration.DEFAULT_SORT_DIRECTION, required = false) String sortDirection) {
        return ResponseEntity.ok(userService.findAllByPage(pageNo, pageSize, sortBy, sortDirection));
    }

    @GetMapping("/search")
    public ResponseEntity<PaginationApiResponse> searchUser(
            @RequestParam(value = "keyword", required = true) String keyword,
            @RequestParam(defaultValue = AppConfiguration.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = AppConfiguration.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "sortBy", defaultValue = AppConfiguration.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = AppConfiguration.DEFAULT_SORT_DIRECTION, required = false) String sortDirection) {

        if (keyword.isEmpty()) {
            throw new MethodArgumentsNotFound("keyword", "search user", keyword);
        }

        return ResponseEntity.ok(userService.searchUser(keyword, pageNo, pageSize, sortBy, sortDirection));
    }

}
