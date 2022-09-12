package com.blogapp.backend.repo;

import com.blogapp.backend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepo extends JpaRepository<Comment, Integer> {

    Boolean existsByUserEmailAndPostTitleAndId(String userEmail, String postTitle, Integer commentId);

    Comment deleteByUserEmailAndPostTitleAndId(String userEmail, String postTitle, Integer commentId);


}
