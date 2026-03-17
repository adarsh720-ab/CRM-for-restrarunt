package com.example.crm.service;

import com.example.crm.entity.dtos.request.SubCategoryRequestDTO;
import com.example.crm.entity.dtos.response.CategoryWithSubCategoriesDTO;
import com.example.crm.entity.dtos.response.SubCategoryResponseDTO;
import com.example.crm.entity.dtos.response.SubCategoryWithItemsDTO;

import java.util.List;
import java.util.UUID;

public interface SubCategoryService {

    SubCategoryResponseDTO createSubCategory(SubCategoryRequestDTO requestDTO);

    SubCategoryResponseDTO getSubCategoryById(UUID subCategoryId);

    // Returns all subcategories grouped by their parent category
    List<CategoryWithSubCategoriesDTO> getAllSubCategoriesGrouped();

    // Returns all subcategories of a specific category (grouped under that one category)
    CategoryWithSubCategoriesDTO getAllSubCategoriesByCategoryId(UUID categoryId);

    // Returns a single subcategory with its items nested inside
    SubCategoryWithItemsDTO getSubCategoryWithItems(UUID subCategoryId);

    // Returns all subcategories of a category, each with items nested inside
    List<SubCategoryWithItemsDTO> getAllSubCategoriesWithItemsByCategoryId(UUID categoryId);

    SubCategoryResponseDTO updateSubCategory(UUID subCategoryId, SubCategoryRequestDTO requestDTO);

    void deleteSubCategory(UUID subCategoryId);
}