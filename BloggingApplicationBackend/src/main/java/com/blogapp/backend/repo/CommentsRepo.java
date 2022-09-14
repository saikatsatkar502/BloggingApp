package com.blogapp.backend.repo;

import com.blogapp.backend.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepo extends JpaRepository<Comment, Integer> {

    Boolean existsByUserEmailIgnoreCaseAndContentIgnoreCase(String userEmail, String content);

    List<Comment> findAllByUserEmailIgnoreCase(String userEmail);

    List<Comment> findAllByPostId(Integer postId);

    List<Comment> findAllByPostIdAndUserEmailIgnoreCase(Integer postId, String userEmail);

    Page<Comment> findAllByPostIdAndUserEmailIgnoreCase(Integer postId, String userEmail,Pageable pageable);

    Page<Comment> findAllByPostId(Integer postId, Pageable pageable);

    Page<Comment> findAllByUserEmailIgnoreCase(String userEmail, Pageable pageable);



    Comment deleteByUserEmailAndId(String userEmail, Integer commentId);


}
