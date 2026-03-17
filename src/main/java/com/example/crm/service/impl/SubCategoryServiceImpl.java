package com.example.crm.service.impl;

import com.example.crm.entity.dtos.request.SubCategoryRequestDTO;
import com.example.crm.entity.dtos.response.CategoryWithSubCategoriesDTO;
import com.example.crm.entity.dtos.response.ItemsResponseDTO;
import com.example.crm.entity.dtos.response.SubCategoryResponseDTO;
import com.example.crm.entity.dtos.response.SubCategoryWithItemsDTO;
import com.example.crm.entity.model.Category;
import com.example.crm.entity.model.SubCategory;
import com.example.crm.exceptions.handlers.DuplicateRecordException;
import com.example.crm.mappers.ItemsMapper;
import com.example.crm.mappers.SubCategoryMapper;
import com.example.crm.repository.CategoryRepository;
import com.example.crm.repository.ItemRepository;
import com.example.crm.repository.SubCategoryRepository;
import com.example.crm.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryMapper subCategoryMapper;
    private final ItemRepository itemRepository;
    private final ItemsMapper itemsMapper;

    @Override
    public SubCategoryResponseDTO createSubCategory(SubCategoryRequestDTO requestDTO) {
        if (subCategoryRepository.existsByName(requestDTO.getName())) {
            throw new DuplicateRecordException("The category with " + requestDTO.getName() + " this name is already exists.");
        }
        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + requestDTO.getCategoryId()));
        SubCategory subCategory = subCategoryMapper.toEntity(requestDTO, category);
        SubCategory saved = subCategoryRepository.save(subCategory);
        return subCategoryMapper.toResponseDTO(saved);
    }

    @Override
    public SubCategoryResponseDTO getSubCategoryById(UUID subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found with id: " + subCategoryId));
        return subCategoryMapper.toResponseDTO(subCategory);
    }

    @Override
    public List<CategoryWithSubCategoriesDTO> getAllSubCategoriesGrouped() {
        Map<Category, List<SubCategory>> grouped = subCategoryRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(SubCategory::getCategory));

        return grouped.entrySet().stream()
                .map(entry -> buildCategoryWithSubCategories(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryWithSubCategoriesDTO getAllSubCategoriesByCategoryId(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        List<SubCategory> subCategories = subCategoryRepository.findByCategoryCategoryId(categoryId);
        return buildCategoryWithSubCategories(category, subCategories);
    }

    @Override
    public SubCategoryWithItemsDTO getSubCategoryWithItems(UUID subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found with id: " + subCategoryId));
        return buildSubCategoryWithItems(subCategory);
    }

    @Override
    public List<SubCategoryWithItemsDTO> getAllSubCategoriesWithItemsByCategoryId(UUID categoryId) {
        return subCategoryRepository.findByCategoryCategoryId(categoryId)
                .stream()
                .map(this::buildSubCategoryWithItems)
                .collect(Collectors.toList());
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private CategoryWithSubCategoriesDTO buildCategoryWithSubCategories(Category category, List<SubCategory> subCategories) {
        List<SubCategoryResponseDTO> subDTOs = subCategories.stream()
                .map(subCategoryMapper::toResponseDTO)
                .collect(Collectors.toList());

        CategoryWithSubCategoriesDTO dto = new CategoryWithSubCategoriesDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        dto.setDescription(category.getDescription());
        dto.setSubCategories(subDTOs);
        return dto;
    }

    private SubCategoryWithItemsDTO buildSubCategoryWithItems(SubCategory subCategory) {
        List<ItemsResponseDTO> items = itemRepository
                .findBySubCategorySubCategoryId(subCategory.getSubCategoryId())
                .stream()
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
        // categoryId and categoryName NOT set here — parent category block shows them
        dto.setItems(items);
        return dto;
    }

    @Override
    public SubCategoryResponseDTO updateSubCategory(UUID subCategoryId, SubCategoryRequestDTO requestDTO) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found with id: " + subCategoryId));
        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + requestDTO.getCategoryId()));

        subCategory.setName(requestDTO.getName());
        subCategory.setSlug(requestDTO.getSlug());
        subCategory.setDescription(requestDTO.getDescription());
        subCategory.setCategory(category);
        subCategory.setActive(requestDTO.getActive());

        SubCategory updated = subCategoryRepository.save(subCategory);
        return subCategoryMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteSubCategory(UUID subCategoryId) {
        subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("Cannot find subcategory to delete."));
        subCategoryRepository.deleteById(subCategoryId);
    }
}