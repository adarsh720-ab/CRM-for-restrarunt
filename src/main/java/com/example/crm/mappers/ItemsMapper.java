package com.example.crm.mappers;

import com.example.crm.entity.dtos.request.ItemsRequestDTO;
import com.example.crm.entity.dtos.response.ItemsResponseDTO;
import com.example.crm.entity.model.Items;
import com.example.crm.entity.model.SubCategory;
import org.springframework.stereotype.Component;

@Component
public class ItemsMapper {

    public Items toEntity(ItemsRequestDTO dto, SubCategory subCategory) {
        Items item = new Items();
        item.setItemName(dto.getItemName());
        item.setItemPrice(dto.getItemPrice());
        item.setItemDescription(dto.getItemDescription());
        item.setSubCategory(subCategory);
        item.setAvailable(dto.getAvailable());
        return item;
    }

    public ItemsResponseDTO toResponseDTO(Items item) {
        ItemsResponseDTO dto = new ItemsResponseDTO();
        dto.setItemId(item.getItemId());
        dto.setItemName(item.getItemName());
        dto.setItemPrice(item.getItemPrice());
        dto.setItemDescription(item.getItemDescription());
        dto.setAvailable(item.getAvailable());
        // subCategoryId and subCategoryName not set — parent is shown above
        return dto;
    }
}