package com.blogapp.backend.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.blogapp.backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

        public User findByEmailIgnoreCase(String email);

        public void deleteByEmailIgnoreCase(String email);

        @Query("SELECT u FROM User u where u.name LIKE %?1% OR u.email LIKE %?1%")
        public Page<User> searchUser(String keyword, Pageable pageable);
}
