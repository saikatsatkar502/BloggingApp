package com.blogapp.backend.service.comments;

import com.blogapp.backend.exception.MethodArgumentsNotFound;
import com.blogapp.backend.exception.ResourceAlreadyExists;
import com.blogapp.backend.exception.ResourceNotFoundException;
import com.blogapp.backend.exception.UnauthorizedException;
import com.blogapp.backend.model.Comment;
import com.blogapp.backend.model.Post;
import com.blogapp.backend.model.User;
import com.blogapp.backend.payloads.CommentRequest;
import com.blogapp.backend.payloads.CommentsResponse;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.repo.CommentsRepo;
import com.blogapp.backend.repo.PostRepository;
import com.blogapp.backend.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentServiceInterface{

    private static final Logger LOGGER = LogManager.getLogger(CommentServiceImpl.class.getName());

    private static final String COMMENT_NOT_FOUND = "Comment not found with ,id :{} ";
    private static final String COMMENT_CREATED = "Comment created";
    private static final String COMMENT_UPDATED = "Comment updated , id;{}";
    private static final String COMMENT_DELETED = "Comment deleted , id: {}";
    private static final String NO_COMMENTS_FOUND = "No comments found";

    private static final String NO_COMMENTS_FOUND_WITH = "No comments found for user with email: ";
    private static final String ALL_COMMENTS_FOUND = "comments found in the database";

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentsRepo commentsRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepo;

    private final LocalDateTime localDateTime = LocalDateTime.now();

    @Override
    public Set<CommentsResponse> getAllComments() {
        Set<CommentsResponse> commentResponse = new HashSet<>();
        List<Comment> comments = commentsRepo.findAll();
        if(comments.isEmpty()){
            LOGGER.error(NO_COMMENTS_FOUND);
            throw new ResourceNotFoundException(NO_COMMENTS_FOUND);
        }
        for (Comment comment: comments
             ) {
            CommentsResponse response= this.convertCommentToCommentResponse(comment);
            commentResponse.add(response);
        }
        LOGGER.info(ALL_COMMENTS_FOUND);
        return commentResponse;

    }

    @Override
    public Set<CommentsResponse> getAllCommentsByPostIdAndUserEmail(String userEmail, Integer postId) {
        Set<CommentsResponse> commentResponse = new HashSet<>();
        List<Comment> comments = commentsRepo.findAllByPostIdAndUserEmailIgnoreCase(postId,userEmail);
        if(comments.isEmpty()){
            LOGGER.error(NO_COMMENTS_FOUND);
            throw new ResourceNotFoundException(NO_COMMENTS_FOUND_WITH+userEmail+" and post with id: "+postId);
        }
        for (Comment comment: comments
        ) {
            CommentsResponse response= this.convertCommentToCommentResponse(comment);
            commentResponse.add(response);
        }
        LOGGER.info(ALL_COMMENTS_FOUND);
        return commentResponse;
    }

    @Override
    public Set<CommentsResponse> getAllCommentsByPostId(Integer postId) {
        if(postId == null){
            LOGGER.error("Post id is null while trying to get all comments by post id");
            throw new MethodArgumentsNotFound("Post id is null");
        }
        Set<CommentsResponse> commentResponse = new HashSet<>();
        List<Comment> comments = commentsRepo.findAllByPostId(postId);
        if(comments.isEmpty()){
            LOGGER.error(NO_COMMENTS_FOUND);
            throw new ResourceNotFoundException(NO_COMMENTS_FOUND+"for post with id: "+postId);
        }
        for (Comment comment: comments
        ) {
            CommentsResponse response= this.convertCommentToCommentResponse(comment);
            commentResponse.add(response);
        }
        LOGGER.info(ALL_COMMENTS_FOUND);
        return commentResponse;
    }

    @Override
    public Set<CommentsResponse> getAllCommentsByUserEmail(String userEmail) {
        if(userEmail == null){
            LOGGER.error("User email is null while trying to get all comments by user email");
            throw new MethodArgumentsNotFound("User email is null");
        }
        Set<CommentsResponse> commentResponse = new HashSet<>();
        List<Comment> comments = commentsRepo.findAllByUserEmailIgnoreCase(userEmail);
        if(comments.isEmpty()){
            LOGGER.error(NO_COMMENTS_FOUND);
            throw new ResourceNotFoundException(NO_COMMENTS_FOUND_WITH+userEmail);
        }
        for (Comment comment: comments
        ) {
            CommentsResponse response= this.convertCommentToCommentResponse(comment);
            commentResponse.add(response);
        }
        LOGGER.info(ALL_COMMENTS_FOUND);
        return commentResponse;
    }

    @Override
    public PaginationApiResponse getAllCommentsPaginationByPostIdAndUserEmail(String userEmail, Integer postId, int page, int size, String sortBy, String direction) {
        if(userEmail.isEmpty() || postId == 0){
            LOGGER.error("User email or post id is null while trying to get all comments by user email and post id");
            throw new MethodArgumentsNotFound("User email or post id is null");
        }
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.fromString((direction)),sortBy));
        Page<Comment> comments = commentsRepo.findAllByPostIdAndUserEmailIgnoreCase(postId,userEmail,pageable);

        if(!comments.hasContent()){
            LOGGER.error(NO_COMMENTS_FOUND);
            throw new ResourceNotFoundException(NO_COMMENTS_FOUND_WITH+userEmail+" and post with id: "+postId);
        }
        return this.convertPageToPageApiResponse(comments);
    }

    @Override
    public PaginationApiResponse getAllCommentsPaginationByPostId(Integer postId, int page, int size, String sortBy, String direction) {
        if(postId == 0){
            LOGGER.error("Post id is null while trying to get all comments by post id");
            throw new MethodArgumentsNotFound("Post id is null");
        }
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.fromString((direction)),sortBy));
        Page<Comment> comments = commentsRepo.findAllByPostId(postId,pageable);
        if(!comments.hasContent()){
            LOGGER.error(NO_COMMENTS_FOUND);
            throw new ResourceNotFoundException(NO_COMMENTS_FOUND+"for post with id: "+postId);
        }
        return this.convertPageToPageApiResponse(comments);
    }

    @Override
    public PaginationApiResponse getAllCommentsPaginationByUserEmail(String userEmail, int page, int size, String sortBy, String direction) {
        if(userEmail.isEmpty()){
            LOGGER.error("User email is null while trying to get all comments by user email");
            throw new MethodArgumentsNotFound("User email is null");
        }
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.fromString((direction)),sortBy));
        Page<Comment> comments = commentsRepo.findAllByUserEmailIgnoreCase(userEmail,pageable);
        if(!comments.hasContent()){
            LOGGER.error(NO_COMMENTS_FOUND);
            throw new ResourceNotFoundException(NO_COMMENTS_FOUND_WITH+userEmail);
        }
        return this.convertPageToPageApiResponse(comments);
    }


    @Override
    public Comment findCommentById(Integer commentId) {
        return this.commentsRepo.findById(commentId).orElseThrow(()->{
            LOGGER.error(COMMENT_NOT_FOUND,commentId);
            throw new ResourceNotFoundException("comment","id",commentId);
        });
    }

    @Override
    public CommentsResponse createComment(CommentRequest request, String userEmail, Integer postId) {
        if(request.getContent()==null || userEmail.isEmpty()|| postId==0){
            LOGGER.error("Method arguments not found for creating comment");
            throw new MethodArgumentsNotFound("Method arguments not found while creating comment");
        }
        User user = this.userService.findByEmail(userEmail);
        Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","id",postId));
        if(Boolean.TRUE.equals(this.commentsRepo.existsByUserEmailIgnoreCaseAndContentIgnoreCase(userEmail,request.getContent()))){
            LOGGER.error("Comment already exists");
            throw new ResourceAlreadyExists("Comment with user : "+userEmail,"content",request.getContent());
        }
        Comment comment = this.convertCommentRequestToComment(request);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(localDateTime);
        comment.setUpdatedAt(localDateTime);
        Comment savedComment = this.commentsRepo.save(comment);
        LOGGER.info(COMMENT_CREATED);
        return this.convertCommentToCommentResponse(savedComment);
    }

    @Override
    public CommentsResponse updateComment(Integer commentId, CommentRequest commentRequest, String userEmail) {
            if(commentId == 0 || commentRequest.getContent()==null || userEmail.isEmpty()){
                LOGGER.error("Method arguments not found for updating comment");
                throw new MethodArgumentsNotFound("Method arguments not found while updating comment");
            }
            User user = this.userService.findByEmail(userEmail);
            Comment comment = this.findCommentById(commentId);
            if(!comment.getUser().getEmail().equalsIgnoreCase(user.getEmail())){
                LOGGER.error("User is not authorized to update this comment");
                throw new UnauthorizedException("User is not authorized to update this comment");
            }
            comment.setContent(commentRequest.getContent());
            comment.setUpdatedAt(localDateTime);
            Comment savedComment = this.commentsRepo.save(comment);
            LOGGER.info(COMMENT_UPDATED,commentId);
            return this.convertCommentToCommentResponse(savedComment);
    }

    @Override
    public CommentsResponse deleteComment(Integer commentId, String userEmail) {
        if(commentId == 0 || userEmail.isEmpty()){
            LOGGER.error("Method arguments not found for deleting comment");
            throw new MethodArgumentsNotFound("Method arguments not found while deleting comment");
        }
        User user = this.userService.findByEmail(userEmail);
        Comment comment = this.findCommentById(commentId);
        if(!comment.getUser().getEmail().equalsIgnoreCase(user.getEmail())){
            LOGGER.error("User is not authorized to delete this comment");
            throw new UnauthorizedException("User is not authorized to delete this comment");
        }
        this.commentsRepo.delete(comment);
        LOGGER.info(COMMENT_DELETED,commentId);
        return this.convertCommentToCommentResponse(comment);
    }

    @Override
    public CommentsResponse convertCommentToCommentResponse(Comment comment) {
        if(comment!=null){
            CommentsResponse response = new CommentsResponse();
            response.setId(comment.getId());
            response.setContent(comment.getContent());
            response.setCreatedAt(comment.getCreatedAt());
            response.setPostId(comment.getPost().getId());
            response.setPostTitle(comment.getPost().getTitle());
            response.setUpdatedAt(comment.getUpdatedAt());
            response.setAuthor(this.userService.convertUserToUserResponse(comment.getUser()));
            return response;
        }
        throw  new MethodArgumentsNotFound("Comment is null");
    }

    @Override
    public Comment convertCommentRequestToComment(CommentRequest commentRequest) {

        return modelMapper.map(commentRequest,Comment.class);
    }

    @Override
    public PaginationApiResponse convertPageToPageApiResponse(Page<Comment> comments) {
        if(comments.isEmpty()){
            LOGGER.error("Comments are empty");
            throw new ResourceNotFoundException("Comments are empty");
        }
        List<Object> postResponses = comments.getContent().stream()
                .map(this::convertCommentToCommentResponse)
                .collect(Collectors.toList());

        PaginationApiResponse response = new PaginationApiResponse();
        response.setPage(comments.getNumber());
        response.setTotalPages(comments.getTotalPages());
        response.setTotalElements(comments.getTotalElements());
        response.setSize(comments.getSize());
        response.setContent(postResponses);
        response.setLastPage(comments.isLast());
        return response;
    }
}
