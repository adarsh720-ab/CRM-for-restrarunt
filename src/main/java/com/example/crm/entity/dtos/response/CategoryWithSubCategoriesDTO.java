package com.example.crm.entity.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CategoryWithSubCategoriesDTO {

    private UUID categoryId;
    private String categoryName;
    private String description;
    private List<SubCategoryResponseDTO> subCategories;
}