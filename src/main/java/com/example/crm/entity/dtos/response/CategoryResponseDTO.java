package com.example.crm.entity.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryResponseDTO {

    private UUID categoryId;
    private String categoryName;
    private String description;
}
