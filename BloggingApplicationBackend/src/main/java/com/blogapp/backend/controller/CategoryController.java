package com.blogapp.backend.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogapp.backend.config.AppConfiguration;
import com.blogapp.backend.exception.ResourceNotFoundException;
import com.blogapp.backend.payloads.CategoryDto;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.service.category.CategoryServiceImpl;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private static final Logger LOGGER = LogManager.getLogger(CategoryController.class);

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/get-all")
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        LOGGER.info("Getting all category");
        List<CategoryDto> categoryList = this.categoryService.getAllCategory();
        if (!categoryList.isEmpty()) {
            return ResponseEntity.ok(categoryList);
        }
        throw new ResourceNotFoundException("No catagories found");
    }

    @GetMapping("/get-by-title/{title}")
    public ResponseEntity<CategoryDto> getCategoryByTitle(@PathVariable String title) {
        LOGGER.info("Getting category by title");
        return ResponseEntity.ok(this.categoryService.getCategoryByTitle(title));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable int id) {
        LOGGER.info("Getting category by id");
        return ResponseEntity.ok(this.categoryService.getCategoryById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<CategoryDto> saveCategory(@Valid @RequestBody CategoryDto category) {
        LOGGER.info("Saving category");
        return ResponseEntity.ok(this.categoryService.saveCategory(category));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryDto category) {
        LOGGER.info("Updating category");
        return ResponseEntity.ok(this.categoryService.updateCategoryById(id, category));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable int id) {
        LOGGER.info("Deleting category");
        return ResponseEntity.ok(this.categoryService.deleteCategoryById(id));
    }

    @GetMapping("/get-by-page")
    public ResponseEntity<PaginationApiResponse> getAllCategoryByPage(
            @RequestParam(value = "pageSize", defaultValue = AppConfiguration.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "pageNo", defaultValue = AppConfiguration.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "sortBy", defaultValue = AppConfiguration.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = AppConfiguration.DEFAULT_SORT_DIRECTION, required = false) String sortDirection) {
        LOGGER.info("Getting all category by page");
        return ResponseEntity.ok(this.categoryService.getAllCategoryByPage(pageNo, pageSize, sortBy, sortDirection));
    }

}
