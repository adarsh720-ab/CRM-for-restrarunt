package com.example.crm.service;

import com.example.crm.entity.dtos.request.CategoryRequestDTO;
import com.example.crm.entity.dtos.response.CategoryResponseDTO;
import com.example.crm.entity.dtos.response.CategoryWithSubCategoriesDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO);

    CategoryResponseDTO getCategoryById(UUID categoryId);

    List<CategoryResponseDTO> getAllCategories();

    // Returns all categories, each with its subcategories nested inside
    List<CategoryWithSubCategoriesDTO> getAllCategoriesWithSubCategories();

    // Returns a single category with its subcategories nested inside
    CategoryWithSubCategoriesDTO getCategoryWithSubCategories(UUID categoryId);

    CategoryResponseDTO updateCategory(UUID categoryId, CategoryRequestDTO requestDTO);

    void deleteCategory(UUID categoryId);
}