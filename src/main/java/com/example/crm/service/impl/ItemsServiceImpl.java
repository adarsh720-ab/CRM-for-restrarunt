package com.example.crm.service.impl;

import com.example.crm.entity.dtos.request.ItemsRequestDTO;
import com.example.crm.entity.dtos.response.ItemsResponseDTO;
import com.example.crm.entity.dtos.response.SubCategoryWithItemsDTO;
import com.example.crm.entity.model.Items;
import com.example.crm.entity.model.SubCategory;
import com.example.crm.exceptions.handlers.ItemNotFoundException;
import com.example.crm.mappers.ItemsMapper;
import com.example.crm.repository.ItemRepository;
import com.example.crm.repository.SubCategoryRepository;
import com.example.crm.service.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemsServiceImpl implements ItemsService {

    private final SubCategoryRepository subCategoryRepository;
    private final ItemsMapper itemsMapper;
    private final ItemRepository itemRepository;

    @Override
    public ItemsResponseDTO createItem(ItemsRequestDTO requestDTO) {
        SubCategory subCategory = subCategoryRepository.findById(requestDTO.getSubCategoryId())
                .orElseThrow(() -> new RuntimeException("SubCategory not found with id: " + requestDTO.getSubCategoryId()));
        Items item = itemsMapper.toEntity(requestDTO, subCategory);
        Items saved = itemRepository.save(item);
        return itemsMapper.toResponseDTO(saved);
    }

    @Override
    public ItemsResponseDTO getItemById(UUID itemId) {
        Items item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found"));
        return itemsMapper.toResponseDTO(item);
    }

    @Override
    public List<SubCategoryWithItemsDTO> getAllItemsGrouped() {
        Map<SubCategory, List<Items>> grouped = itemRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Items::getSubCategory));

        return grouped.entrySet().stream()
                .map(entry -> buildSubCategoryWithItems(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public SubCategoryWithItemsDTO getItemsBySubCategoryId(UUID subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found with id: " + subCategoryId));
        List<Items> items = itemRepository.findBySubCategorySubCategoryId(subCategoryId);
        return buildSubCategoryWithItems(subCategory, items);
    }

    // ── helper ───────────────────────────────────────────────────────────────

    private SubCategoryWithItemsDTO buildSubCategoryWithItems(SubCategory subCategory, List<Items> items) {
        List<ItemsResponseDTO> itemDTOs = items.stream()
                .map(itemsMapper::toResponseDTO)
                .collect(Collectors.toList());

        SubCategoryWithItemsDTO dto = new SubCategoryWithItemsDTO();
        dto.setSubCategoryId(subCategory.getSubCategoryId());
        dto.setName(subCategory.getName());
        dto.setSlug(subCategory.getSlug());
        dto.setDescription(subCategory.getDescription());
        dto.setActive(subCategory.getActive());
        dto.setCreatedAt(subCategory.getCreatedAt());
        dto.setUpdatedAt(subCategory.getUpdatedAt());
        // categoryId and categoryName NOT set — parent subcategory block shows them
        dto.setItems(itemDTOs);
        return dto;
    }

    @Override
    public ItemsResponseDTO updateItem(UUID itemId, ItemsRequestDTO requestDTO) {
        Items item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found"));
        SubCategory subCategory = subCategoryRepository.findById(requestDTO.getSubCategoryId())
                .orElseThrow(() -> new ItemNotFoundException("SubCategory not found"));

        item.setItemName(requestDTO.getItemName());
        item.setItemPrice(requestDTO.getItemPrice());
        item.setItemDescription(requestDTO.getItemDescription());
        item.setSubCategory(subCategory);
        item.setAvailable(requestDTO.getAvailable());

        Items updated = itemRepository.save(item);
        return itemsMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteItem(UUID itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ItemNotFoundException("Item not found to delete");
        }
        itemRepository.deleteById(itemId);
    }
}