package com.example.crm.mappers;

import com.example.crm.entity.dtos.request.SubCategoryRequestDTO;
import com.example.crm.entity.dtos.response.SubCategoryResponseDTO;
import com.example.crm.entity.model.Category;
import com.example.crm.entity.model.SubCategory;
import org.springframework.stereotype.Component;

@Component
public class SubCategoryMapper {

    public SubCategory toEntity(SubCategoryRequestDTO request, Category category) {

        SubCategory subCategory = new SubCategory();
        subCategory.setName(request.getName());
        subCategory.setSlug(request.getSlug());
        subCategory.setDescription(request.getDescription());
        subCategory.setCategory(category);
        subCategory.setActive(request.getActive());
        return subCategory;
    }

    public SubCategoryResponseDTO toResponseDTO(SubCategory subCategory) {

        SubCategoryResponseDTO dto = new SubCategoryResponseDTO();
        dto.setSubCategoryId(subCategory.getSubCategoryId());
        dto.setName(subCategory.getName());
        dto.setSlug(subCategory.getSlug());
        dto.setDescription(subCategory.getDescription());
        dto.setActive(subCategory.getActive());
        dto.setCreatedAt(subCategory.getCreatedAt());
        dto.setUpdatedAt(subCategory.getUpdatedAt());
        // categoryId and categoryName not set — parent is shown above
        return dto;
    }
}