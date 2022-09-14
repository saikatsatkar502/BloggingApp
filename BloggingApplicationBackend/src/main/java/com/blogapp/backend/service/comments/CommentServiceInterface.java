package com.blogapp.backend.service.comments;


import com.blogapp.backend.model.Comment;
import com.blogapp.backend.payloads.CommentRequest;
import com.blogapp.backend.payloads.CommentsResponse;
import com.blogapp.backend.payloads.PaginationApiResponse;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface CommentServiceInterface {
    Set<CommentsResponse> getAllComments();
    // find all comments of a specific user of a particular post
    Set<CommentsResponse> getAllCommentsByPostIdAndUserEmail(String userEmail, Integer postId);
    //find all comments related to a specific post
    Set<CommentsResponse> getAllCommentsByPostId(Integer postId);

    //find all comments related to a specific user
    Set<CommentsResponse> getAllCommentsByUserEmail(String userEmail);

    PaginationApiResponse getAllCommentsPaginationByPostIdAndUserEmail(String userEmail, Integer postId, int page, int size, String sortBy,String direction);

    PaginationApiResponse getAllCommentsPaginationByPostId(Integer postId, int page, int size, String sortBy,String direction);

    PaginationApiResponse getAllCommentsPaginationByUserEmail(String userEmail, int page, int size, String sortBy,String direction);
    Comment findCommentById(Integer commentId);
    CommentsResponse createComment(CommentRequest request, String userEmail, Integer postId);

    CommentsResponse updateComment(Integer commentId, CommentRequest commentRequest, String userEmail);

    CommentsResponse deleteComment(Integer commentId, String userEmail);

    CommentsResponse convertCommentToCommentResponse(Comment comment);

    Comment convertCommentRequestToComment(CommentRequest commentRequest);

    PaginationApiResponse convertPageToPageApiResponse(Page<Comment> comments);



}
