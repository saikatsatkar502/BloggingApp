package com.blogapp.backend.service.user;

import java.util.List;

import com.blogapp.backend.model.User;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.payloads.UserRequest;
import com.blogapp.backend.payloads.UserResponse;

public interface UserServiceInterface {

	public List<UserResponse> findAll();

	public UserResponse save(UserRequest userRequest);

	public User findByEmail(String email);

	public User findById(int id);

	public UserResponse delete(int id);

	public UserResponse update(UserRequest userReq, int id);

	public User convertUserRequestToUser(UserRequest userRequest);

	public UserRequest convertUserToUserRequest(User user);

	public UserResponse convertUserToUserResponse(User user);

	public UserResponse deleteByEmail(String email);

	public PaginationApiResponse findAllByPage(int page, int size);

}
