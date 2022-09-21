package com.blogapp.backend.controller;

import com.blogapp.backend.config.AppConfiguration;
import com.blogapp.backend.exception.MethodArgumentsNotFound;
import com.blogapp.backend.exception.ResourceNotFoundException;
import com.blogapp.backend.exception.UnauthorizedException;
import com.blogapp.backend.model.User;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.payloads.UserRequest;
import com.blogapp.backend.payloads.UserResponse;
import com.blogapp.backend.security.JwtTokenHelper;
import com.blogapp.backend.service.role.RoleServiceImpl;
import com.blogapp.backend.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
@ConstructorBinding
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired private RoleServiceImpl roleService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @GetMapping("/get-all")
    public ResponseEntity<List<UserResponse>> findAll() throws ResourceNotFoundException {
        List<UserResponse> users = userService.findAll();
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        }
        throw new ResourceNotFoundException("User List is empty ");

    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id, @RequestHeader(value = "Authorization") String token) throws ResourceNotFoundException {
        String username = jwtTokenHelper.extractUsername(token.substring(7));
        if (id != 0) {
            User user = userService.findById(id);
            if(user.getEmail().equals(username) || this.userService.findByEmail(username).getRoles().contains(roleService.findRoleByName("ROLE_ADMIN"))){
                return new ResponseEntity<>(user, HttpStatus.OK);
            }else{
                throw new UnauthorizedException(username+" : you are not authorized to access this resource");
            }
        } else {
            throw new MethodArgumentsNotFound("User Id is Not Given to search");
        }

    }

    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email,
                                               @RequestHeader(value = "Authorization") String token, Principal principal) throws ResourceNotFoundException {
        String username = jwtTokenHelper.extractUsername(token.substring(7));
        if (email.isEmpty()) {
            throw new MethodArgumentsNotFound("User email is Not Given to search");
        }
        User user = userService.findByEmail(email);
        if (user != null ) {
            if(user.getEmail().equals(username) || this.userService.findByEmail(username).getRoles().contains(roleService.findRoleByName("ROLE_ADMIN"))){
                return new ResponseEntity<>(user, HttpStatus.OK);
            }else{
                throw new UnauthorizedException(username+" : you are not authorized to access this resource");
            }
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
    public ResponseEntity<UserResponse> updateUser(@PathVariable int id, @Valid @RequestBody UserRequest user,
                                                   @RequestHeader(value = "Authorization") String token) throws ResourceNotFoundException {
        String username = jwtTokenHelper.extractUsername(token.substring(7));
        if (user != null && id != 0) {
            UserResponse userResponse = userService.update(user, id,username);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            throw new MethodArgumentsNotFound("User ", "update", user);
        }

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable int id) {
        if (id != 0) {
            UserResponse userResponse = userService.delete(id);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            throw new MethodArgumentsNotFound("Id", "delete user", id);
        }

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-by-email/{email}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable String email) {
        if (email.isEmpty()) {
            UserResponse userResponse = userService.deleteByEmail(email);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            throw new MethodArgumentsNotFound("Email", "delete user", email);
        }

    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all-by-page")
    public ResponseEntity<PaginationApiResponse> findAllByPage(
            @RequestParam(defaultValue = AppConfiguration.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = AppConfiguration.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "sortBy", defaultValue = AppConfiguration.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = AppConfiguration.DEFAULT_SORT_DIRECTION, required = false) String sortDirection) {
        return ResponseEntity.ok(userService.findAllByPage(pageNo, pageSize, sortBy, sortDirection));
    }
    @PreAuthorize("hasRole('ADMIN')")
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
