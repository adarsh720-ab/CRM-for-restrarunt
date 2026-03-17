package com.example.crm.controller.table;

import com.example.crm.entity.dtos.request.TableRequestDTO;
import com.example.crm.entity.dtos.response.TableResponseDTO;
import com.example.crm.service.TableService;
import com.example.crm.util.enums.TableStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/Table")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping("/create-table")
    public ResponseEntity<TableResponseDTO> createTable(@Valid @RequestBody TableRequestDTO request){

        return ResponseEntity.status(HttpStatus.CREATED).body(tableService.createTable(request));
    }

    @GetMapping("/get-all-tables")
    public ResponseEntity<List<TableResponseDTO>> getAllTables(){
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @GetMapping("/get-table/table-number/{tableNumber}")
    public ResponseEntity<TableResponseDTO> getTableByTableNumber(@Valid @PathVariable Integer tableNumber){

        return ResponseEntity.ok(tableService.getTableByTableNumber(tableNumber));
    }

    @GetMapping("/get-table/status/{status}")
    public ResponseEntity<List<TableResponseDTO>> getTableByStatus(@PathVariable TableStatus status){

        return ResponseEntity.ok(tableService.getTableByStatus(status));
    }

    @GetMapping("/get-table/{id}")
    public ResponseEntity<TableResponseDTO> getTableById(@PathVariable UUID id){

        return ResponseEntity.ok(tableService.getTableById(id));
    }

    @PutMapping("/update-table/{tableId}")
    public ResponseEntity<TableResponseDTO> updateTable(@PathVariable UUID tableId, @Valid @RequestBody TableRequestDTO requestDTO){

        return ResponseEntity.ok(tableService.updateTable(tableId, requestDTO));
    }

    @PatchMapping("/{tableId}/status")
    public ResponseEntity<TableResponseDTO> updateTableStatus(@PathVariable UUID tableId, @Valid @RequestBody TableStatus status){

        return ResponseEntity.ok(tableService.updateTableStatus(tableId, status));
    }

    @DeleteMapping("/delete-table/{tableId}")
    public ResponseEntity<Void> deleteTable(@PathVariable UUID tableId){
        tableService.deleteTable(tableId);
        return ResponseEntity.noContent().build();
    }



}
