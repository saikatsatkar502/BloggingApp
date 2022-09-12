package com.blogapp.backend.service.posts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blogapp.backend.exception.MethodArgumentsNotFound;
import com.blogapp.backend.exception.ResourceAlreadyExists;
import com.blogapp.backend.exception.ResourceNotFoundException;
import com.blogapp.backend.exception.UnauthorizedException;
import com.blogapp.backend.model.Post;
import com.blogapp.backend.payloads.CategoryDto;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.payloads.PostRequest;
import com.blogapp.backend.payloads.PostResponse;
import com.blogapp.backend.repo.PostRepository;
import com.blogapp.backend.service.category.CategoryServiceImpl;
import com.blogapp.backend.service.user.UserService;

@Service
public class PostServiceImpl implements PostServiceInterface {

    private static final Logger LOGGER = LogManager.getLogger(PostServiceImpl.class);

    private static final String POSTS = "Posts";
    private static final String POST_NOT_FOUND = "Post not found ";
    private static final String POST_FOUND = "Post found";
    private static final String ALL_POST_FOUND = "All Post found";
    private static final String POST_DELETED = "Post deleted with id : {}";
    private static final String POST_UPDATED = "Post updated with id : {}";
    private static final String POST_CREATED = "Post created with id : {}";

    private static final String POST_ID = "Post Id is required";
    private static final String TITLE = "title";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private UserService userService;

    private LocalDateTime now = LocalDateTime.now();

    @Override
    public List<PostResponse> getAllPosts() {
        List<PostResponse> postResponses = new ArrayList<>();
        List<Post> postList = this.postRepository.findAll();
        if (!postList.isEmpty()) {
            for (Post post : postList) {
                PostResponse postResponse = this.convertPostToPostResponse(post);
                postResponses.add(postResponse);
            }
            LOGGER.info(ALL_POST_FOUND);
            return postResponses;
        }
        LOGGER.error(POST_NOT_FOUND);
        throw new ResourceNotFoundException(POST_NOT_FOUND);

    }

    @Override
    public List<PostResponse> getPostsByCategory(String categoryTitle) {
        if (categoryTitle != null) {
            if (this.categoryService.getCategoryByTitle(categoryTitle) != null) {
                List<PostResponse> postResponses = new ArrayList<>();
                List<Post> postList = this.postRepository.findAllByCategoryTitleIgnoreCase(categoryTitle);
                if (!postList.isEmpty()) {
                    for (Post post : postList) {
                        PostResponse postResponse = this.convertPostToPostResponse(post);
                        postResponses.add(postResponse);
                    }
                    LOGGER.info(ALL_POST_FOUND);
                    return postResponses;
                }
                LOGGER.error(POST_NOT_FOUND);
                throw new ResourceNotFoundException(POSTS, "category", categoryTitle);
            }
            LOGGER.error("Category not found");
            throw new ResourceNotFoundException("Category", TITLE, categoryTitle);
        }
        LOGGER.error("Category title is null");
        throw new MethodArgumentsNotFound("Category title not found");
    }

    @Override
    public List<PostResponse> getPostsByAuthor(Integer id) {
        if (id != null) {
            List<PostResponse> postResponses = new ArrayList<>();
            List<Post> postList = this.postRepository.findByAuthorId(id);
            if (!postList.isEmpty()) {
                for (Post post : postList) {
                    PostResponse postResponse = this.convertPostToPostResponse(post);
                    postResponses.add(postResponse);
                }
                LOGGER.info(ALL_POST_FOUND);
                return postResponses;
            }
            LOGGER.error(POST_NOT_FOUND);
            throw new ResourceNotFoundException(POSTS, "author", id);
        }
        LOGGER.error("Author id is null");
        throw new MethodArgumentsNotFound("Author id not found");
    }

    @Override
    public PostResponse getPostById(Integer id) {
        if (id != null) {
            Post post = this.postRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
            PostResponse postResponse = this.convertPostToPostResponse(post);
            LOGGER.info(POST_FOUND);
            return postResponse;
        }
        LOGGER.error(POST_ID);
        throw new MethodArgumentsNotFound("Post id not found");
    }

    @Override
    public List<PostResponse> getPostsByAuthorEmail(String email) {
        if (email != null) {
            List<PostResponse> postResponses = new ArrayList<>();
            List<Post> postList = this.postRepository.findByAuthorEmailIgnoreCase(email);
            if (!postList.isEmpty()) {
                for (Post post : postList) {
                    PostResponse postResponse = this.convertPostToPostResponse(post);
                    postResponses.add(postResponse);
                }
                LOGGER.info(ALL_POST_FOUND);
                return postResponses;
            }
            LOGGER.error(POST_NOT_FOUND);
            throw new ResourceNotFoundException(POSTS, "author", email);
        }
        LOGGER.error("Author email is null");
        throw new MethodArgumentsNotFound("Author email not found");
    }

    @Override
    public PostResponse createPost(PostRequest postRequest, String userEmail) {
        if (postRequest != null && !userEmail.isEmpty()) {
            if (Boolean.FALSE.equals(this.postRepository.existsByTitleIgnoreCase(postRequest.getTitle()))) {
                Post post = this.convertPostRequestToPost(postRequest);
                post.setAuthor(this.userService.findByEmail(userEmail));
                post.setCreatedAt(now);
                LOGGER.info(POST_CREATED, post.getId());
                return this.convertPostToPostResponse(this.postRepository.save(post));
            }
            LOGGER.error("Post already exists");
            throw new ResourceAlreadyExists("post", TITLE, postRequest.getTitle());
        }
        throw new MethodArgumentsNotFound("Post Request", "create post", postRequest);
    }

    @Override
    public PostResponse updatePost(PostRequest postRequest, Integer id, String userEmail) {
        if (postRequest != null && !userEmail.isEmpty() && id > 0) {
            Post oldPost = this.postRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
            if (oldPost.getAuthor().getEmail().equalsIgnoreCase(userEmail)) {
                Post post = this.convertPostRequestToPost(postRequest);
                oldPost.setTitle(post.getTitle());
                oldPost.setContent(post.getContent());
                oldPost.setImage(post.getImage());
                oldPost.setCategory(post.getCategory());
                oldPost.setCreatedAt(now);
                LOGGER.info(POST_UPDATED, oldPost.getId());
                return this.convertPostToPostResponse(this.postRepository.save(oldPost));
            }
            LOGGER.error("You are not authorized to update this post");
            throw new UnauthorizedException(String.format("%s is not authorized to update %s with title : %s",
                    userEmail, "this post", oldPost.getTitle()));
        }
        LOGGER.error("update post Request is null");
        throw new MethodArgumentsNotFound("Post Request", "update post", postRequest);
    }

    @Override
    public PostResponse deletePost(Integer id) {
        if (id != null) {
            Post post = this.postRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
            this.postRepository.delete(post);
            LOGGER.info(POST_DELETED, post.getId());
            return this.convertPostToPostResponse(post);
        }
        LOGGER.error(POST_ID);
        throw new MethodArgumentsNotFound("Post id", "delete post", id);
    }

    @Override
    public PostResponse convertPostToPostResponse(Post post) {

        PostResponse postRes = new PostResponse();
        postRes.setId(post.getId());
        postRes.setTitle(post.getTitle());
        postRes.setContent(post.getContent());
        postRes.setCreatedAt(post.getCreatedAt());
        postRes.setImage(post.getImage());
        postRes.setCategory(categoryService.convertCategoryToCatagaoryDto(post.getCategory()));
        postRes.setAuthor(userService.convertUserToUserResponse(post.getAuthor()));
        return postRes;
    }

    @Override
    public Post convertPostRequestToPost(PostRequest postRequest) {

        Post post = new Post();
        CategoryDto categoryDto = categoryService.getCategoryByTitle(postRequest.getCategoryTitle());
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setImage(postRequest.getImage());
        post.setCategory(categoryService.convertCategoryDtoToCategory(categoryDto));
        return post;
    }

    // pagination

    @Override
    public PaginationApiResponse getAllPostsByPagination(int page, int size, String sortBy, String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<Post> postPage = this.postRepository.findAll(pageable);
        if (postPage.getContent().isEmpty()) {
            LOGGER.error(POST_NOT_FOUND);
            throw new ResourceNotFoundException("Post", "page", page);
        }
        return this.convertPageToPageApiResponse(postPage);

    }

    @Override
    public PaginationApiResponse getPostsByCategoryByPagination(String categoryTitle, int page, int size, String sortBy,
            String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<Post> postPage = this.postRepository.findAllByCategoryTitleIgnoreCase(categoryTitle, pageable);
        if (postPage.getContent().isEmpty()) {
            LOGGER.error(POST_NOT_FOUND);
            throw new ResourceNotFoundException("Post", "page", page);
        }
        return this.convertPageToPageApiResponse(postPage);
    }

    @Override
    public PaginationApiResponse getPostsByAuthorEmailByPagination(String authorEmail, int page, int size,
            String sortBy,
            String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<Post> postPage = this.postRepository.findAllByAuthorEmailIgnoreCase(authorEmail, pageable);
        if (postPage.getContent().isEmpty()) {
            LOGGER.error(POST_NOT_FOUND);
            throw new ResourceNotFoundException("Post", "page", page);
        }
        return this.convertPageToPageApiResponse(postPage);
    }

    @Override
    public PaginationApiResponse convertPageToPageApiResponse(Page<Post> postPage) {
        if (postPage.getContent().isEmpty()) {
            LOGGER.error(POST_NOT_FOUND);
            throw new MethodArgumentsNotFound(POSTS, "convert post response to pagination", postPage.getContent());
        }
        List<Object> postResponses = postPage.getContent().stream()
                .map(this::convertPostToPostResponse)
                .collect(Collectors.toList());

        PaginationApiResponse paginationApiResponse = new PaginationApiResponse();
        paginationApiResponse.setTotalPages(postPage.getTotalPages());
        paginationApiResponse.setTotalElements(postPage.getTotalElements());
        paginationApiResponse.setPage(postPage.getNumber());
        paginationApiResponse.setSize(postPage.getSize());
        paginationApiResponse.setLastPage(postPage.isLast());
        paginationApiResponse.setContent(postResponses);

        return paginationApiResponse;

    }

    @Override
    public PaginationApiResponse searchPostByKeywordWithPagination(String keyword, int page, int size, String sort,
            String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<Post> postPage = this.postRepository.searchPostByKeyword(keyword, pageable);
        if (!postPage.hasContent()) {
            LOGGER.error(POST_NOT_FOUND + "with keyword :{}", keyword);
            throw new ResourceNotFoundException(POSTS, "Search Keyword", keyword);
        }
        return this.convertPageToPageApiResponse(postPage);

    }

    @Override
    public PostResponse updatePostImage(String image, Integer id) {
        if (id != null) {
            Post post = this.postRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
            post.setImage(image);
            LOGGER.info(POST_UPDATED, post.getId());
            return this.convertPostToPostResponse(this.postRepository.save(post));
        }
        LOGGER.error(POST_ID);
        throw new MethodArgumentsNotFound("Post id", "update post image", id);
    }

    @Override
    public Post findPostByAuthorEmailAndTitle(String email, String title) {
        if(email != null && title != null) {
            return this.postRepository.findByAuthorEmailIgnoreCaseAndTitleIgnoreCase(email, title);

        }
        throw new MethodArgumentsNotFound("email or title not found in findPostByAuthorEmailAndTitle method");

    }

}
