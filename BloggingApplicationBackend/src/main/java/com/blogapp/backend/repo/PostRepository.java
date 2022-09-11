package com.blogapp.backend.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.blogapp.backend.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByCategoryTitleIgnoreCase(String categoryTitle);

    List<Post> findByAuthorId(Integer id);

    List<Post> findByAuthorEmailIgnoreCase(String email);

    Post findByAuthorEmailIgnoreCaseAndTitleIgnoreCase(String email, String title);

    Boolean existsByTitleIgnoreCase(String title);

    Page<Post> findAllByCategoryTitleIgnoreCase(String categoryTitle, Pageable pageable);

    Page<Post> findAllByAuthorEmailIgnoreCase(String categoryTitle, Pageable pageable);

    // search post by keyword

    @Query("SELECT p FROM Post p WHERE p.title LIKE %?1% OR p.id LIKE %?1% OR p.category.title LIKE %?1% OR p.author.name LIKE %?1%")
    Page<Post> searchPostByKeyword(String keyword, Pageable pageable);

}
