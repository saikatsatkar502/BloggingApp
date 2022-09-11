package com.blogapp.backend.service.category;

import java.util.List;

import com.blogapp.backend.model.Category;
import com.blogapp.backend.payloads.CategoryDto;
import com.blogapp.backend.payloads.PaginationApiResponse;

public interface CategoryServiceInterface {

    public CategoryDto saveCategory(CategoryDto category);

    public CategoryDto getCategoryById(int id);

    public CategoryDto getCategoryByTitle(String title);

    public CategoryDto deleteCategoryById(int id);

    public CategoryDto updateCategoryById(int id, CategoryDto category);

    public List<CategoryDto> getAllCategory();

    public List<Category> getAllCategoryByTitlesList(List<String> categoryTitle);

    public CategoryDto convertCategoryToCatagaoryDto(Category category);

    public Category convertCategoryDtoToCategory(CategoryDto categoryDto);

    public PaginationApiResponse getAllCategoryByPage(int page, int size, String sortBy, String direction);
}
