package com.blogapp.backend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogapp.backend.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    public Category findByTitleIgnoreCase(String title);

    public List<Category> findAllByTitleIgnoreCase(String title);
}
