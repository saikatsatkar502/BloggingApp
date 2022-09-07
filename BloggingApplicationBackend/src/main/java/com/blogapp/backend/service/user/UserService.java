package com.blogapp.backend.service.user;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogapp.backend.exception.MethodArgumentsNotFound;
import com.blogapp.backend.exception.ResourceAlreadyExists;
import com.blogapp.backend.exception.ResourceNotFoundException;
import com.blogapp.backend.model.User;
import com.blogapp.backend.payloads.UserRequest;
import com.blogapp.backend.payloads.UserResponse;
import com.blogapp.backend.repo.UserRepository;

@Service
public class UserService implements UserServiceInterface {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class.getName());
    private static final String USER_NOT_FOUND = "User not found with ";
    private static final String EMAIL = "email";
    private static final String USER_CREATED = "User created";
    private static final String USER_UPDATED = "User updated";
    private static final String USER_DELETED = "User deleted , id: {}";
    private static final String USER_FOUND = "User found , :{}";
    @Autowired
    private final UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<UserResponse> findAll() {
        // List<UserResponse> userResponseList = new
        // ArrayList<>(Collections.emptyList());
        List<User> users = userRepo.findAll();
        if (!users.isEmpty()) {
            return users.stream().map(this::convertUserToUserResponse).collect(Collectors.toList());

        }
        // for (User user : users) {
        // UserResponse userResponse = this.convertUserToUserResponse(user);
        // userResponseList.add(userResponse);
        // }

        return Collections.emptyList();
    }

    @Override
    public UserResponse save(UserRequest userRequest) {
        if (userRequest != null) {
            if (this.userRepo.findByEmailIgnoreCase(userRequest.getEmail()) == null) {
                User user = userRepo.save(this.convertUserRequestToUser(userRequest));
                LOGGER.info(USER_CREATED);
                return this.convertUserToUserResponse(user);
            } else {
                throw new ResourceAlreadyExists("User", EMAIL, userRequest.getEmail());
            }
        } else {
            throw new MethodArgumentsNotFound("UserRequest is null");
        }

    }

    @Override
    public User findByEmail(String email) {

        if (!email.isEmpty()) {
            User user = userRepo.findByEmailIgnoreCase(email);
            if (user != null) {
                LOGGER.info(USER_FOUND, user.getEmail());
                return userRepo.findByEmailIgnoreCase(email);
            } else {
                throw new ResourceNotFoundException("user", EMAIL, email);
            }
        } else {
            throw new MethodArgumentsNotFound("Email", "findById", email);
        }

    }

    @Override
    public User findById(int id) {

        if (id > 0) {
            User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
            LOGGER.info(USER_FOUND, user.getEmail());
            return user;
        } else {
            throw new MethodArgumentsNotFound("Id", "findById", id);
        }

    }

    @Override
    public UserResponse delete(int id) {
        if (id > 0) {
            User user = this.userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
            if (user != null) {
                userRepo.deleteById(id);
                LOGGER.info(USER_DELETED, id);
                return this.convertUserToUserResponse(user);
            } else {
                throw new ResourceNotFoundException(USER_NOT_FOUND);
            }
        } else {
            throw new MethodArgumentsNotFound("Id is null");
        }

    }

    @Override
    public UserResponse update(UserRequest userReq, int id) {

        if (userReq != null && id > 0) {
            User user = this.userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
            if (user != null) {
                user.setId(id);
                user.setEmail(userReq.getEmail());
                user.setName(userReq.getName());
                user.setPassword(userReq.getPassword());
                userRepo.save(user);
                LOGGER.info(USER_UPDATED);
                return this.convertUserToUserResponse(user);
            } else {
                throw new ResourceNotFoundException(USER_NOT_FOUND);
            }
        } else {
            throw new MethodArgumentsNotFound("UserRequest or Id is null");
        }
    }

    @Override
    public User convertUserRequestToUser(UserRequest userRequest) {
        return modelMapper.map(userRequest, User.class);
    }

    @Override
    public UserRequest convertUserToUserRequest(User user) {
        return modelMapper.map(user, UserRequest.class);

    }

    @Override
    public UserResponse convertUserToUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);

    }

    @Override
    public UserResponse deleteByEmail(String email) {
        if (email.isEmpty()) {
            User user = this.userRepo.findByEmailIgnoreCase(email);
            if (user != null) {
                userRepo.deleteByEmailIgnoreCase(email);
                LOGGER.info(USER_DELETED, email);
                return this.convertUserToUserResponse(user);
            } else {
                throw new ResourceNotFoundException("User", EMAIL, email);
            }
        } else {
            throw new MethodArgumentsNotFound("Email", "deleteByEmail", email);
        }
    }

}
