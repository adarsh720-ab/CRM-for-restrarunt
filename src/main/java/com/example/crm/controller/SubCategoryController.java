package com.example.crm.controller;

import com.example.crm.entity.dtos.request.SubCategoryRequestDTO;
import com.example.crm.entity.dtos.response.CategoryWithSubCategoriesDTO;
import com.example.crm.entity.dtos.response.SubCategoryResponseDTO;
import com.example.crm.entity.dtos.response.SubCategoryWithItemsDTO;
import com.example.crm.service.SubCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sub-category")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @PostMapping("/create")
    public ResponseEntity<SubCategoryResponseDTO> createSubCategory(@Valid @RequestBody SubCategoryRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subCategoryService.createSubCategory(requestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubCategoryResponseDTO> getSubCategoryById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryById(id));
    }

    // GET /sub-category
    // All subcategories grouped by category — no redundancy
    @GetMapping
    public ResponseEntity<List<CategoryWithSubCategoriesDTO>> getAllSubCategoriesGrouped() {
        return ResponseEntity.ok(subCategoryService.getAllSubCategoriesGrouped());
    }


    // All subcategories of one category, grouped under that category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryWithSubCategoriesDTO> getAllSubCategoriesByCategoryId(
            @PathVariable("categoryId") UUID categoryId) {
        return ResponseEntity.ok(subCategoryService.getAllSubCategoriesByCategoryId(categoryId));
    }

    // GET /sub-category/{id}/with-items
    // Single subcategory with its items nested inside
    @GetMapping("/{id}/with-items")
    public ResponseEntity<SubCategoryWithItemsDTO> getSubCategoryWithItems(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryWithItems(id));
    }

    // GET /sub-category/category/{categoryId}/with-items
    // All subcategories of a category, each with items nested inside
    @GetMapping("/category/{categoryId}/with-items")
    public ResponseEntity<List<SubCategoryWithItemsDTO>> getAllSubCategoriesWithItemsByCategoryId(
            @PathVariable("categoryId") UUID categoryId) {
        return ResponseEntity.ok(subCategoryService.getAllSubCategoriesWithItemsByCategoryId(categoryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubCategoryResponseDTO> updateSubCategory(
            @PathVariable("id") UUID subCategoryId,
            @Valid @RequestBody SubCategoryRequestDTO requestDTO) {
        return ResponseEntity.ok(subCategoryService.updateSubCategory(subCategoryId, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubCategory(@PathVariable("id") UUID id) {
        subCategoryService.deleteSubCategory(id);
        return ResponseEntity.ok("Subcategory deleted.");
    }
}