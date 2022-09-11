package com.blogapp.backend.service.posts;

import java.util.List;

import org.springframework.data.domain.Page;

import com.blogapp.backend.model.Post;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.payloads.PostRequest;
import com.blogapp.backend.payloads.PostResponse;

public interface PostServiceInterface {
    public PostResponse createPost(PostRequest postRequest, String userEmail);

    public PostResponse updatePost(PostRequest postRequest, Integer id, String userEmail);

    public PostResponse deletePost(Integer id);

    public List<PostResponse> getAllPosts();

    public List<PostResponse> getPostsByCategory(String categoryTitle);

    public List<PostResponse> getPostsByAuthor(Integer id);

    public PostResponse getPostById(Integer id);

    public List<PostResponse> getPostsByAuthorEmail(String email);

    public PostResponse convertPostToPostResponse(Post post);

    public Post convertPostRequestToPost(PostRequest postRequest);

    public PaginationApiResponse getAllPostsByPagination(int page, int size, String sort, String direction);

    public PaginationApiResponse getPostsByCategoryByPagination(String categoryTitle, int page, int size, String sort,
            String direction);

    public PaginationApiResponse getPostsByAuthorEmailByPagination(String authorEmail, int page, int size, String sort,
            String direction);

    public PaginationApiResponse convertPageToPageApiResponse(Page<Post> postPage);

    public PaginationApiResponse searchPostByKeywordWithPagination(String keyword, int page, int size, String sort,
            String direction);

}
