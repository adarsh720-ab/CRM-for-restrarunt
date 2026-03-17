package com.example.crm.entity.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SubCategoryWithItemsDTO {

    private UUID subCategoryId;
    private String name;
    private String slug;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // categoryId and categoryName removed — parent category is already shown above
    private List<ItemsResponseDTO> items;
}