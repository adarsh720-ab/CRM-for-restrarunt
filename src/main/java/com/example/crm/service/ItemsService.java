package com.example.crm.service;

import com.example.crm.entity.dtos.request.ItemsRequestDTO;
import com.example.crm.entity.dtos.response.ItemsResponseDTO;
import com.example.crm.entity.dtos.response.SubCategoryWithItemsDTO;

import java.util.List;
import java.util.UUID;

public interface ItemsService {

    ItemsResponseDTO createItem(ItemsRequestDTO requestDTO);

    ItemsResponseDTO getItemById(UUID itemId);

    // Returns all items grouped by their parent subcategory
    List<SubCategoryWithItemsDTO> getAllItemsGrouped();

    // Returns all items of a specific subcategory (grouped under that one subcategory)
    SubCategoryWithItemsDTO getItemsBySubCategoryId(UUID subCategoryId);

    ItemsResponseDTO updateItem(UUID itemId, ItemsRequestDTO requestDTO);

    void deleteItem(UUID itemId);
}