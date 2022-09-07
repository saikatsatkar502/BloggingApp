package com.blogapp.backend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogapp.backend.model.Catagory;

@Repository
public interface CatagoryRepository extends JpaRepository<Catagory, Integer> {

    public Catagory findByTitleIgnoreCase(String title);

    public List<Catagory> findAllByTitleIgnoreCase(String title);
}
