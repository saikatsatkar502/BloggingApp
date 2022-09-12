package com.blogapp.backend.service.comments;

import com.blogapp.backend.model.Comment;
import com.blogapp.backend.payloads.CommentRequest;
import com.blogapp.backend.payloads.CommentsResponse;
import com.blogapp.backend.payloads.PaginationApiResponse;

import java.util.Set;

public interface CommentServiceInterface {
    CommentsResponse createComment(CommentRequest commentRequest, String userEmail, String postTitle);

    CommentsResponse updateComment(CommentRequest commentRequest, String userEmail, String postTitle, Integer commentId);

    CommentsResponse deleteComment(String userEmail, String postTitle, Integer commentId);

    Set<CommentsResponse> getAllComments();

    CommentsResponse getComment(Integer commentId);

    Set<CommentsResponse> getAllCommentsByUser(String userEmail);

    Set<CommentsResponse> getAllCommentsByPost(String postTitle);

    PaginationApiResponse getAllCommentsPagination(String userEmail, String postTitle, Integer pageNo, Integer pageSize, String sortBy, String orderBy);

    PaginationApiResponse getAllCommentsPaginationByUser(String userEmail, Integer pageNo, Integer pageSize, String sortBy, String orderBy);

    PaginationApiResponse getAllCommentsPaginationByPost(String postTitle, Integer pageNo, Integer pageSize, String sortBy, String orderBy);

    CommentsResponse convertCommentToCommentResponse(Comment comment);

    Comment convertCommentRequestToComment(CommentRequest commentRequest);



}
