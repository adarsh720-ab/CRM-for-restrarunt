package com.example.crm.controller;

import com.example.crm.entity.dtos.request.CategoryRequestDTO;
import com.example.crm.entity.dtos.response.CategoryResponseDTO;
import com.example.crm.entity.dtos.response.CategoryWithSubCategoriesDTO;
import com.example.crm.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
        CategoryResponseDTO response = categoryService.createCategory(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // GET /Category/with-subcategories
    // Returns all categories, each with subcategories nested as an array inside
    @GetMapping("/with-subcategories")
    public ResponseEntity<List<CategoryWithSubCategoriesDTO>> getAllCategoriesWithSubCategories() {
        return ResponseEntity.ok(categoryService.getAllCategoriesWithSubCategories());
    }

    // GET /Category/{id}/with-subcategories
    // Returns a single category with its subcategories nested inside
    @GetMapping("/{id}/with-subcategories")
    public ResponseEntity<CategoryWithSubCategoriesDTO> getCategoryWithSubCategories(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryWithSubCategories(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category Successfully deleted.");
    }
}