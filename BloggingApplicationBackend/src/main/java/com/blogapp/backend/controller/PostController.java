package com.blogapp.backend.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

import com.blogapp.backend.exception.UnableToUploadImageException;
import com.blogapp.backend.exception.UnauthorizedException;
import com.blogapp.backend.model.Post;
import com.blogapp.backend.security.JwtTokenHelper;
import com.blogapp.backend.service.file.FileServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import com.blogapp.backend.config.AppConfiguration;
import com.blogapp.backend.exception.MethodArgumentsNotFound;
import com.blogapp.backend.exception.ResourceNotFoundException;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.payloads.PostRequest;
import com.blogapp.backend.payloads.PostResponse;
import com.blogapp.backend.service.posts.PostServiceImpl;
import com.blogapp.backend.service.user.UserService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/post")
public class PostController {

    private static final Logger LOGGER = LogManager.getLogger(PostController.class);

    private static final String POST_ID = "Post id";
    private static final String USER_EMAIL = "email";

    @Value("${project.image}")
    private String path;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileServiceImpl fileService;

    @GetMapping("/get-all")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        LOGGER.info("Getting all posts");
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PostMapping("/create/{userEmail}")
    public ResponseEntity<PostResponse> createPost(@PathVariable String userEmail,
                                                   @RequestBody PostRequest postRequest,
                                                    @RequestHeader(value = "Authorization") String token) throws ResourceNotFoundException
    {
        LOGGER.info("Creating post");

        if (!userEmail.isEmpty() && postRequest != null) {
            if (!userEmail.equalsIgnoreCase(jwtTokenHelper.extractUsername(token.substring(7)))) {
                throw new UnauthorizedException("You are not authorized to create post for this user :" + userEmail);
            }
            if (this.userService.findByEmail(userEmail) != null) {
                return ResponseEntity.ok(postService.createPost(postRequest, userEmail));
            }
            throw new ResourceNotFoundException("User", USER_EMAIL, userEmail);
        }
        throw new MethodArgumentsNotFound("User Email ", "create Post", userEmail);

    }

    @GetMapping("/get/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Integer postId) {
        LOGGER.info("Getting post");
        if (postId != null) {

            return ResponseEntity.ok(postService.getPostById(postId));
        }
        throw new MethodArgumentsNotFound(POST_ID, "get Post by id", postId);
    }

    @GetMapping("/get-by-category/{categoryTitle}")
    public ResponseEntity<List<PostResponse>> getPostByCategory(@PathVariable String categoryTitle) {
        LOGGER.info("Getting post by category");
        if (!categoryTitle.isEmpty()) {
            return ResponseEntity.ok(postService.getPostsByCategory(categoryTitle));
        }
        throw new MethodArgumentsNotFound("Category Title ", "get Post by category title", categoryTitle);
    }

    @GetMapping("/get-by-author-email/{userEmail}")
    public ResponseEntity<List<PostResponse>> getPostByAuthor(@PathVariable String userEmail) {
        LOGGER.info("Getting post by author");
        if (!userEmail.isEmpty()) {
            if (this.userService.findByEmail(userEmail) != null) {
                return ResponseEntity.ok(postService.getPostsByAuthorEmail(userEmail));
            }
            throw new ResourceNotFoundException("User", USER_EMAIL, userEmail);
        }
        throw new MethodArgumentsNotFound("User Email ", "get Post by author email", userEmail);
    }

    @GetMapping("/get-by-author-id/{userId}")
    public ResponseEntity<List<PostResponse>> getPostByAuthorId(@PathVariable Integer userId) {
        LOGGER.info("Getting post by author id");
        if (userId != null) {
            if (this.userService.findById(userId) != null) {
                return ResponseEntity.ok(postService.getPostsByAuthor(userId));
            }
            throw new ResourceNotFoundException("User", "id", userId);
        }
        throw new MethodArgumentsNotFound("User Id ", "get Post by author id", userId);
    }

    @PutMapping("/update/{postId}/{userEmail}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Integer postId, @PathVariable String userEmail,
                                                   @RequestBody PostRequest postRequest,
                                                   @RequestHeader(value = "Authorization") String token
                                                   ) {

        LOGGER.info("Updating post");
        if (postId != null && postRequest != null) {
            if(!userEmail.equalsIgnoreCase(jwtTokenHelper.extractUsername(token.substring(7)))){
                throw new UnauthorizedException("You are not authorized to update post for this user :" + userEmail);
            }
            if (this.userService.findByEmail(userEmail) != null) {
                return ResponseEntity.ok(postService.updatePost(postRequest, postId, userEmail));
            }
            throw new ResourceNotFoundException("User", USER_EMAIL, userEmail);
        }
        throw new MethodArgumentsNotFound(POST_ID, "update Post", postId);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable Integer postId) {
        LOGGER.info("Deleting post");
        if (postId != null) {
            return ResponseEntity.ok(postService.deletePost(postId));
        }
        throw new MethodArgumentsNotFound(POST_ID, "delete Post", postId);

    }

    @GetMapping("get-by-page")
    public ResponseEntity<PaginationApiResponse> getPostByPage(
            @RequestParam(value = "pageNo", defaultValue = AppConfiguration.DEFAULT_PAGE_NUMBER, required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConfiguration.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConfiguration.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = AppConfiguration.DEFAULT_SORT_DIRECTION, required = false) String sortDirection) {
        LOGGER.info("Getting post by page");
        if (pageNo != null && pageSize != null) {
            return ResponseEntity.ok(postService.getAllPostsByPagination(pageNo, pageSize, sortBy, sortDirection));
        }
        throw new MethodArgumentsNotFound("Page No ", "get Post by page", pageNo);
    }

    @GetMapping("/get-by-category/{categoryTitle}/page")
    public ResponseEntity<PaginationApiResponse> getPostByCategoryPagination(
            @PathVariable String categoryTitle,
            @RequestParam(value = "pageNo", defaultValue = AppConfiguration.DEFAULT_PAGE_NUMBER, required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConfiguration.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConfiguration.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = AppConfiguration.DEFAULT_SORT_DIRECTION, required = false) String sortDirection) {
        LOGGER.info("Getting post by category and page");
        if (!categoryTitle.isEmpty()) {
            return ResponseEntity.ok(
                    postService.getPostsByCategoryByPagination(categoryTitle, pageNo, pageSize, sortBy, sortDirection));
        }
        throw new MethodArgumentsNotFound("Category Title ", "get Post by category title", categoryTitle);
    }

    @GetMapping("/get-by-authorEmail/{authorEmail}/page")
    public ResponseEntity<PaginationApiResponse> getPostByAuthorEmailPagination(
            @PathVariable String authorEmail,
            @RequestParam(value = "pageNo", defaultValue = AppConfiguration.DEFAULT_PAGE_NUMBER, required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConfiguration.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConfiguration.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = AppConfiguration.DEFAULT_SORT_DIRECTION, required = false) String sortDirection) {
        LOGGER.info("Getting post by author email and page");
        if (!authorEmail.isEmpty()) {
            return ResponseEntity.ok(postService.getPostsByAuthorEmailByPagination(authorEmail, pageNo, pageSize,
                    sortBy, sortDirection));
        }
        throw new MethodArgumentsNotFound("Author Email ", "get Post by Author Email", authorEmail);
    }

    @GetMapping("/search")
    public ResponseEntity<PaginationApiResponse> searchPosts(
            @RequestParam(value = "keyword", required = true) String keyword,
            @RequestParam(value = "pageNo", defaultValue = AppConfiguration.DEFAULT_PAGE_NUMBER, required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConfiguration.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConfiguration.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = AppConfiguration.DEFAULT_SORT_DIRECTION, required = false) String sortDirection) {
        if (keyword.isEmpty()) {
            LOGGER.error("Keyword Not Found");
            throw new MethodArgumentsNotFound("Keyword", "search posts", keyword);
        }
        return ResponseEntity.ok(
                this.postService.searchPostByKeywordWithPagination(keyword, pageNo, pageSize, sortBy, sortDirection));

    }

    @PostMapping("/upload-image")
    public ResponseEntity<PostResponse> uploadImage(
            @RequestParam(value = "image", required = true) MultipartFile imageFile,
            @RequestParam(value = "postTitle", required = true) String postTitle,
            @RequestParam(value = "authorEmail", required = true) String authorEmail,
            @RequestHeader(value = "Authorization") String token
    ) throws IOException {
        if (imageFile.isEmpty() || postTitle.isEmpty() || authorEmail.isEmpty()) {
            LOGGER.error("Image File, Post Title or Author Email Not Found");
            throw new MethodArgumentsNotFound("Image File, Post Title or Author Email", "upload image", imageFile);
        }
        if(!authorEmail.equalsIgnoreCase(jwtTokenHelper.extractUsername(token.substring(7)))){
            throw new UnauthorizedException("You are not authorized to upload image for this user :" + authorEmail);
        }
        Post post = this.postService.findPostByAuthorEmailAndTitle(authorEmail, postTitle);
        if (post != null) {
            String fileName = this.fileService.uploadImage(path, imageFile);
            PostResponse postResponse = this.postService.updatePostImage(fileName, post.getId());
            if (postResponse != null) {
                return new ResponseEntity<>(postResponse, HttpStatus.OK);
            } else {
                throw new UnableToUploadImageException(fileName, post.getTitle());
            }

        }
        throw new ResourceNotFoundException("Post", "Post Title", postTitle);


    }

    @GetMapping(value = "/view-image/{imageName}/", produces = MediaType.IMAGE_JPEG_VALUE)
    public void serveImage(
            @PathVariable String imageName,
            @PathParam(value = "postId") Integer postId,
            HttpServletResponse response
    ) throws IOException {

        if (imageName.isEmpty() || postId == 0) {
            LOGGER.error("Image Name or Post Id Not Found");
            throw new MethodArgumentsNotFound("Image Name or Post Id", "view image", imageName);
        }
        PostResponse postResponse = this.postService.getPostById(postId);
        if (postResponse != null) {
            if (postResponse.getImage().equalsIgnoreCase(imageName)) {
                InputStream resource = this.fileService.getResource(path, imageName);
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                StreamUtils.copy(resource, response.getOutputStream());
            } else {
                throw new ResourceNotFoundException("Post", "Image Name", imageName);
            }
        } else {
            throw new ResourceNotFoundException("Post", "Post Id", postId);
        }


    }

}
