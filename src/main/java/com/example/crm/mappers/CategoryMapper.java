package com.example.crm.mappers;

import com.example.crm.entity.dtos.request.CategoryRequestDTO;
import com.example.crm.entity.dtos.response.CategoryResponseDTO;
import com.example.crm.entity.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDTO requestDTO) {
        Category category = new Category();
        category.setCategoryName(requestDTO.getCategoryName());
        category.setDescription(requestDTO.getDescription());
        return category;
    }

    public CategoryResponseDTO toResponseDTO(Category category) {
        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setCategoryId(category.getCategoryId());
        response.setCategoryName(category.getCategoryName());
        response.setDescription(category.getDescription());
        return response;
    }
}