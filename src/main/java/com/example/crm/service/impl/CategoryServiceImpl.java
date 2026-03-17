package com.example.crm.service.impl;

import com.example.crm.entity.dtos.request.CategoryRequestDTO;
import com.example.crm.entity.dtos.response.CategoryResponseDTO;
import com.example.crm.entity.dtos.response.CategoryWithSubCategoriesDTO;
import com.example.crm.entity.dtos.response.SubCategoryResponseDTO;
import com.example.crm.entity.model.Category;
import com.example.crm.entity.model.SubCategory;
import com.example.crm.exceptions.handlers.CategoryNotFoundException;
import com.example.crm.exceptions.handlers.DuplicateRecordException;
import com.example.crm.mappers.CategoryMapper;
import com.example.crm.mappers.SubCategoryMapper;
import com.example.crm.repository.CategoryRepository;
import com.example.crm.repository.SubCategoryRepository;
import com.example.crm.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final SubCategoryRepository subCategoryRepository;
    private final SubCategoryMapper subCategoryMapper;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {

        if (categoryRepository.existsByCategoryName(requestDTO.getCategoryName())) {
            throw new DuplicateRecordException("The Category with this name Already Exists.");
        }
        Category category = categoryMapper.toEntity(requestDTO);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponseDTO(savedCategory);
    }

    @Override
    public CategoryResponseDTO getCategoryById(UUID categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        return categoryMapper.toResponseDTO(category);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryWithSubCategoriesDTO> getAllCategoriesWithSubCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::buildCategoryWithSubCategories)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryWithSubCategoriesDTO getCategoryWithSubCategories(UUID categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
        return buildCategoryWithSubCategories(category);
    }

    private CategoryWithSubCategoriesDTO buildCategoryWithSubCategories(Category category) {

        List<SubCategoryResponseDTO> subCategories = subCategoryRepository
                .findByCategoryCategoryId(category.getCategoryId())
                .stream()
                .map(subCategoryMapper::toResponseDTO)
                .collect(Collectors.toList());

        CategoryWithSubCategoriesDTO dto = new CategoryWithSubCategoriesDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        dto.setDescription(category.getDescription());
        dto.setSubCategories(subCategories);
        return dto;
    }

    @Override
    public CategoryResponseDTO updateCategory(UUID categoryId, CategoryRequestDTO requestDTO) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        category.setCategoryName(requestDTO.getCategoryName());
        category.setDescription(requestDTO.getDescription());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponseDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(UUID categoryId) {

        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("The Category with this id Not Found.");
        }
        categoryRepository.deleteById(categoryId);
    }
}