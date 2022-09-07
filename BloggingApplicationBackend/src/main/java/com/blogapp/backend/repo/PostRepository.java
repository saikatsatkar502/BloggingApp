package com.blogapp.backend.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogapp.backend.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByCatagoryTitleIgnoreCase(String catagoryTitle);

    List<Post> findByAuthorId(Integer id);

    List<Post> findByAuthorEmailIgnoreCase(String email);

    Post findByAuthorEmailIgnoreCaseAndTitleIgnoreCase(String email, String title);

    Boolean existsByTitleIgnoreCase(String title);

    Page<Post> findAllByCatagoryTitleIgnoreCase(String catagoryTitle, Pageable pageable);

    Page<Post> findAllByAuthorEmailIgnoreCase(String catagoryTitle, Pageable pageable);

}
