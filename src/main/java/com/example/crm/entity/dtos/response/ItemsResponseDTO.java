package com.example.crm.entity.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ItemsResponseDTO {

    private UUID itemId;
    private String itemName;
    private Float itemPrice;
    private String itemDescription;
    private Boolean available;
    // subCategoryId and subCategoryName removed — parent is already shown above
}