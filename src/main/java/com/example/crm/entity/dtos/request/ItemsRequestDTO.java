package com.example.crm.entity.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ItemsRequestDTO {

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotNull(message = "Item price is required")
    @Positive(message = "Item price must be positive")
    private Float itemPrice;

    private String itemDescription;

    @NotNull(message = "SubCategory ID is required")
    private UUID subCategoryId;

    private Boolean available = true;
}