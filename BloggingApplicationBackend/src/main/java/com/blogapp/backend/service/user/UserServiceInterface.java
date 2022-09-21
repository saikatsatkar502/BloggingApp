package com.blogapp.backend.service.user;

import java.util.List;

import org.springframework.data.domain.Page;

import com.blogapp.backend.model.User;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.payloads.UserRequest;
import com.blogapp.backend.payloads.UserResponse;

public interface UserServiceInterface {

	 List<UserResponse> findAll();

	 UserResponse save(UserRequest userRequest);

	UserResponse registerUser(UserRequest userRequest);

	User findByEmail(String email);

	User findById(int id);

	UserResponse delete(int id);

	UserResponse update(UserRequest userReq, int id, String requestUserEmail);

	User convertUserRequestToUser(UserRequest userRequest);

	UserRequest convertUserToUserRequest(User user);

	UserResponse convertUserToUserResponse(User user);

	UserResponse deleteByEmail(String email);

	PaginationApiResponse findAllByPage(int page, int size, String sort, String direction);

	PaginationApiResponse searchUser(String keyword, int page, int size, String sort, String direction);

	PaginationApiResponse convertUserPageToPageApiResponse(Page<User> userPage);

}
