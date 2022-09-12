package com.blogapp.backend.service.comments;

import com.blogapp.backend.exception.ResourceNotFoundException;
import com.blogapp.backend.model.Comment;
import com.blogapp.backend.payloads.CommentRequest;
import com.blogapp.backend.payloads.CommentsResponse;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.repo.CommentsRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class CommentServiceImpl implements CommentServiceInterface{

    @Autowired private CommentsRepo commentsRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CommentsResponse createComment(CommentRequest commentRequest, String userEmail, String postTitle) {
        return null;
    }

    @Override
    public CommentsResponse updateComment(CommentRequest commentRequest, String userEmail, String postTitle, Integer commentId) {
        return null;
    }

    @Override
    public CommentsResponse deleteComment(String userEmail, String postTitle, Integer commentId) {
        if(userEmail.isEmpty() || postTitle.isEmpty() || commentId == 0){
            throw new IllegalArgumentException("User email, post title and comment id cannot be empty");
        }
        if(!commentsRepo.existsByUserEmailAndPostTitleAndId(userEmail, postTitle, commentId)){
            throw new ResourceNotFoundException("Comment", "id", commentId);
        }
        return this.convertCommentToCommentResponse(this.commentsRepo.deleteByUserEmailAndPostTitleAndId(userEmail, postTitle, commentId));

    }

    @Override
    public Set<CommentsResponse> getAllComments() {
        return null;
    }

    @Override
    public CommentsResponse getComment(Integer commentId) {
        return null;
    }

    @Override
    public Set<CommentsResponse> getAllCommentsByUser(String userEmail) {
        return null;
    }

    @Override
    public Set<CommentsResponse> getAllCommentsByPost(String postTitle) {
        return null;
    }

    @Override
    public PaginationApiResponse getAllCommentsPagination(String userEmail, String postTitle, Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
        return null;
    }

    @Override
    public PaginationApiResponse getAllCommentsPaginationByUser(String userEmail, Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
        return null;
    }

    @Override
    public PaginationApiResponse getAllCommentsPaginationByPost(String postTitle, Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
        return null;
    }

    @Override
    public CommentsResponse convertCommentToCommentResponse(Comment comment) {
        return modelMapper.map(comment, CommentsResponse.class);
    }

    @Override
    public Comment convertCommentRequestToComment(CommentRequest commentRequest) {
        return modelMapper.map(commentRequest, Comment.class);
    }
}
