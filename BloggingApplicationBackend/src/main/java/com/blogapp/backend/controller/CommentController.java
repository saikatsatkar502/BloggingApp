package com.blogapp.backend.controller;

import com.blogapp.backend.config.AppConfiguration;
import com.blogapp.backend.payloads.CommentRequest;
import com.blogapp.backend.payloads.CommentsResponse;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.service.comments.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentServiceImpl commentService;

    @GetMapping("/get-all")
    public ResponseEntity<Set<CommentsResponse>> getAllComments(){
        return ResponseEntity.ok(this.commentService.getAllComments());
    }

    @PostMapping("/create")
    public ResponseEntity<CommentsResponse> createComment(
            @RequestParam(value = "userEmail",required = true) String userEmail,
            @RequestParam(value = "postId" , required = true) Integer postId,
            @RequestBody CommentRequest comment
            ){
        return ResponseEntity.ok(this.commentService.createComment(comment,userEmail,postId));
    }

    @GetMapping("/get-by-post-id")
    public ResponseEntity<Set<CommentsResponse>> getCommentsByPostId(
            @RequestParam(value = "postId",required = true) Integer postId
    ){
        if(postId == null || postId < 1){
            throw new IllegalArgumentException("Post id cannot be null");
        }
        return ResponseEntity.ok(this.commentService.getAllCommentsByPostId(postId));
    }


    @GetMapping("/get-by-user-email")
    public ResponseEntity<Set<CommentsResponse>> getCommentsByUserEmail(
            @RequestParam(value = "userEmail",required = true) String userEmail
    ){
        if(userEmail == null || userEmail.isEmpty()){
            throw new IllegalArgumentException("User email cannot be null");
        }
        return ResponseEntity.ok(this.commentService.getAllCommentsByUserEmail(userEmail));
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<CommentsResponse> deleteComment(
            @PathVariable(value = "commentId",required = true) Integer commentId,
            @RequestParam(value = "userEmail",required = true) String userEmail
    ){
        if(commentId == null || commentId < 1){
            throw new IllegalArgumentException("Comment id cannot be null");
        }
        return ResponseEntity.ok(this.commentService.deleteComment(commentId,userEmail));
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<CommentsResponse> updateComment(
            @PathVariable(value = "commentId",required = true) Integer commentId,
            @RequestParam(value = "userEmail",required = true) String userEmail,
            @RequestBody CommentRequest commentRequest
    ){
        if(commentId == null || commentId < 1){
            throw new IllegalArgumentException("Comment id cannot be null");
        }
        return ResponseEntity.ok(this.commentService.updateComment(commentId,commentRequest,userEmail));
    }

    @GetMapping("/get-by-post-id/page")
    public ResponseEntity<PaginationApiResponse> getCommentsByPostIdWithPagination(
            @RequestParam(value = "postId",required = true) Integer postId,
            @RequestParam(value = "pageNo",required = true,defaultValue = AppConfiguration.DEFAULT_PAGE_NUMBER) Integer pageNo,
            @RequestParam(value = "pageSize",required = true, defaultValue = "5") Integer pageSize,
            @RequestParam(value = "sortBy",required = true, defaultValue = AppConfiguration.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDirection",required = true, defaultValue = AppConfiguration.DEFAULT_SORT_DIRECTION) String sortDirection
    ){
        if(postId == null || postId < 1){
            throw new IllegalArgumentException("Post id cannot be null");
        }
        return ResponseEntity.ok(this.commentService.getAllCommentsPaginationByPostId(postId,pageNo,pageSize,sortBy,sortDirection));
    }

    @GetMapping("/get-by-user-email/page")
    public ResponseEntity<PaginationApiResponse> getCommentsByUserEmailWithPagination(
            @RequestParam(value = "userEmail",required = true) String userEmail,
            @RequestParam(value = "pageNo",required = true, defaultValue = "0") Integer pageNo,
            @RequestParam(value = "pageSize",required = true , defaultValue = "5") Integer pageSize,
            @RequestParam(value = "sortBy",required = true,defaultValue = AppConfiguration.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDirection",required = true, defaultValue = AppConfiguration.DEFAULT_SORT_DIRECTION) String sortDirection
    ){
        if(userEmail == null || userEmail.isEmpty()){
            throw new IllegalArgumentException("User email cannot be null");
        }
        return ResponseEntity.ok(this.commentService.getAllCommentsPaginationByUserEmail(userEmail,pageNo,pageSize,sortBy,sortDirection));
    }


}
