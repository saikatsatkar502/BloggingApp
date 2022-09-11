package com.blogapp.backend.service.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blogapp.backend.exception.MethodArgumentsNotFound;
import com.blogapp.backend.exception.ResourceAlreadyExists;
import com.blogapp.backend.exception.ResourceNotFoundException;
import com.blogapp.backend.model.Category;
import com.blogapp.backend.payloads.CategoryDto;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.repo.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryServiceInterface {

    private static final Logger LOGGER = LogManager.getLogger(CategoryServiceImpl.class);

    private static final String CATAGORY = "category";
    private static final String TITLE = "title";
    private static final String INVALID_ID = "Invalid id : {}";
    private static final String CATAGORY_NOT_FOUND_ID = "Category not found with id : {}";
    private static final String CATAGORY_FOUND_ID = "Category found with id : {}";
    private static final String CATAGORY_NOT_FOUND_TITLE = "Category not found with title : {}";
    private static final String CATAGORY_FOUND_TITLE = "Category found with title : {}";
    private static final String CATAGORY_DELETED = "Category deleted with id : {}";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto saveCategory(CategoryDto category) {

        if (category != null) {
            if (this.categoryRepository.findByTitleIgnoreCase(category.getTitle()) == null) {
                Category categoryEntity = this.convertCategoryDtoToCategory(category);

                return convertCategoryToCatagaoryDto(this.categoryRepository.save(categoryEntity));
            }
            throw new ResourceAlreadyExists(CATAGORY, TITLE, category.getTitle());
        }

        throw new ResourceNotFoundException(CATAGORY, "save Category", category);
    }

    @Override
    public CategoryDto getCategoryById(int id) {

        if (id > 0) {
            Category category = this.categoryRepository.findById(id).orElse(null);

            if (category != null) {
                LOGGER.info(CATAGORY_FOUND_ID, id);
                return this.convertCategoryToCatagaoryDto(category);
            }
            LOGGER.error(CATAGORY_NOT_FOUND_ID, id);
            throw new ResourceNotFoundException(CATAGORY, "Id", id);
        }
        LOGGER.error(INVALID_ID, id);
        throw new MethodArgumentsNotFound("Id", "find category by id", id);
    }

    @Override
    public CategoryDto getCategoryByTitle(String title) {

        if (!title.isEmpty()) {
            Category category = this.categoryRepository.findByTitleIgnoreCase(title);

            if (category != null) {
                LOGGER.info(CATAGORY_FOUND_TITLE, title);
                return this.convertCategoryToCatagaoryDto(category);
            }
            LOGGER.error(CATAGORY_NOT_FOUND_TITLE, title);
            throw new ResourceNotFoundException(CATAGORY, TITLE, title);
        }
        LOGGER.error("Title is empty");
        throw new MethodArgumentsNotFound(TITLE, "find category by Title", title);
    }

    @Override
    public CategoryDto deleteCategoryById(int id) {

        if (id > 0) {
            Category category = this.categoryRepository.findById(id).orElseThrow(() -> {
                LOGGER.error(CATAGORY_NOT_FOUND_ID, id);
                return new ResourceNotFoundException(CATAGORY, "Id", id);
            });

            if (category != null) {
                this.categoryRepository.delete(category);
                LOGGER.info(CATAGORY_DELETED, id);
                return this.convertCategoryToCatagaoryDto(category);
            }
        }
        LOGGER.error(INVALID_ID, id);
        throw new MethodArgumentsNotFound("Id", "delete category by id", id);

    }

    @Override
    public CategoryDto updateCategoryById(int id, CategoryDto category) {

        if (id > 0) {
            Category oldCategory = this.categoryRepository.findById(id).orElseThrow(() -> {
                LOGGER.error(CATAGORY_NOT_FOUND_ID, id);
                return new ResourceNotFoundException(CATAGORY, "Id", id);
            });

            if (oldCategory != null && category.getTitle().equals(oldCategory.getTitle())) {
                oldCategory.setDescription(category.getDescription());
                return this.convertCategoryToCatagaoryDto(this.categoryRepository.save(oldCategory));
            }
            throw new ResourceNotFoundException(CATAGORY, TITLE, category.getTitle());
        }

        LOGGER.error(INVALID_ID, id);
        throw new MethodArgumentsNotFound("Id", "update category by id", id);

    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        List<Category> catagories = this.categoryRepository.findAll();
        if (!catagories.isEmpty()) {
            for (Category category : catagories) {
                categoryDtos.add(this.convertCategoryToCatagaoryDto(category));
            }
            LOGGER.info("Catagories found : {}", categoryDtos.size());
            return categoryDtos;
        }
        LOGGER.info("No catagories found");
        return Collections.emptyList();
    }

    @Override
    public CategoryDto convertCategoryToCatagaoryDto(Category category) {

        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public Category convertCategoryDtoToCategory(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    @Override
    public List<Category> getAllCategoryByTitlesList(List<String> categoryTitle) {
        List<Category> catagories = new ArrayList<>();
        for (String title : categoryTitle) {
            catagories.add(this.categoryRepository.findByTitleIgnoreCase(title));
        }
        return catagories;
    }

    @Override
    public PaginationApiResponse getAllCategoryByPage(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<Category> catagories = this.categoryRepository.findAll(pageable);
        if (!catagories.hasContent()) {
            LOGGER.error("No catagories found");
            throw new ResourceNotFoundException(CATAGORY, "page", page);
        }
        List<Object> categoryList = catagories.getContent().stream()
                .map(this::convertCategoryToCatagaoryDto).collect(Collectors.toList());
        PaginationApiResponse paginationApiResponse = new PaginationApiResponse();
        paginationApiResponse.setTotalPages(catagories.getTotalPages());
        paginationApiResponse.setTotalElements(catagories.getTotalElements());
        paginationApiResponse.setPage(catagories.getNumber());
        paginationApiResponse.setSize(catagories.getSize());
        paginationApiResponse.setLastPage(catagories.isLast());
        paginationApiResponse.setContent(categoryList);
        return paginationApiResponse;
    }

}
