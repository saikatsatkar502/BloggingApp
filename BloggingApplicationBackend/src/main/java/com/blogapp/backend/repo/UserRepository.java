package com.blogapp.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogapp.backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

        public User findByEmailIgnoreCase(String email);

        public void deleteByEmailIgnoreCase(String email);
}
