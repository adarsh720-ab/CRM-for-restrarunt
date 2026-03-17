package com.example.crm.controller;

import com.example.crm.entity.dtos.request.ItemsRequestDTO;
import com.example.crm.entity.dtos.response.ItemsResponseDTO;
import com.example.crm.entity.dtos.response.SubCategoryWithItemsDTO;
import com.example.crm.service.ItemsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemsController {

    private final ItemsService itemsService;

    @PostMapping("/create")
    public ResponseEntity<ItemsResponseDTO> createItem(@Valid @RequestBody ItemsRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemsService.createItem(requestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemsResponseDTO> getItemById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(itemsService.getItemById(id));
    }

    // GET /items
    // All items grouped by subcategory — no redundancy
    @GetMapping
    public ResponseEntity<List<SubCategoryWithItemsDTO>> getAllItemsGrouped() {
        return ResponseEntity.ok(itemsService.getAllItemsGrouped());
    }

    // All items of one subcategory, grouped under that subcategory
    @GetMapping("/sub-category/{subCategoryId}")
    public ResponseEntity<SubCategoryWithItemsDTO> getItemsBySubCategoryId(
            @PathVariable("subCategoryId") UUID subCategoryId) {
        return ResponseEntity.ok(itemsService.getItemsBySubCategoryId(subCategoryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemsResponseDTO> updateItem(
            @PathVariable("id") UUID id,
            @Valid @RequestBody ItemsRequestDTO requestDTO) {
        return ResponseEntity.ok(itemsService.updateItem(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable("id") UUID id) {
        itemsService.deleteItem(id);
        return ResponseEntity.ok("Item deleted Successfully.");
    }
}