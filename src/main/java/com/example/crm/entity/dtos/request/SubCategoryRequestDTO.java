package com.example.crm.entity.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SubCategoryRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Slug is required")
    private String slug;

    private String description;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    private Boolean active = true;
}