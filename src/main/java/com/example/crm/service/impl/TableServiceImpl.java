package com.example.crm.service.impl;

import com.example.crm.entity.Table;
import com.example.crm.entity.dtos.request.TableRequestDTO;
import com.example.crm.entity.dtos.response.TableResponseDTO;
import com.example.crm.mappers.TableMapper;
import com.example.crm.repository.RestaurantTableRepository;
import com.example.crm.service.TableService;
import com.example.crm.util.enums.TableStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final RestaurantTableRepository repository;
    private final TableMapper tableMapper;

    @Override
    public TableResponseDTO createTable(TableRequestDTO requestDTO) {
        if (repository.existsByTableNumber(requestDTO.getTableNumber())){
            throw new RuntimeException("Table Already Exists.");
        }
        Table table = tableMapper.toEntity(requestDTO);
        Table savedTable = repository.save(table);
        return tableMapper.toResponse(savedTable);
    }

    @Override
    public List<TableResponseDTO> getAllTables() {

        return repository.findAll()
                .stream()
                .map(tableMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TableResponseDTO getTableByTableNumber(Integer tableNumber) {
        return repository.findByTableNumber(tableNumber)
                .map(tableMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Table not found with tableNumber: " + tableNumber));
    }

    @Override
    public List<TableResponseDTO> getTableByStatus(TableStatus status) {

        return repository.findByStatus(status)
                .stream()
                .map(tableMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TableResponseDTO getTableById(UUID tableId) {
        return repository.findById(tableId)
                .map(tableMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Table not found with id: " + tableId));
    }

    @Override
    public TableResponseDTO updateTable(UUID tableId, TableRequestDTO requestDTO) {
        log.info("Updating table with id: {}", tableId);

        Table table = repository.findById(tableId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Table not found with id: " + tableId));

        if (!table.getTableNumber().equals(requestDTO.getTableNumber())
                && repository.existsByTableNumber(requestDTO.getTableNumber())) {
            throw new IllegalArgumentException(
                    "Table number " + requestDTO.getTableNumber() + " already exists");
        }

        tableMapper.updateEntity(table, requestDTO);
        Table updated = repository.save(table);

        log.info("Table updated successfully with id: {}", updated.getTableId());
        return tableMapper.toResponse(updated);
    }

    @Override
    public TableResponseDTO updateTableStatus(UUID tableId, TableStatus status) {

        Table table = repository.findById(tableId)
                .orElseThrow(()-> new  EntityNotFoundException(
                        "Table not found with id: " + tableId));
        table.setStatus(status);
        return tableMapper.toResponse(repository.save(table));
    }




    @Override
    public void deleteTable(UUID tableId) {

        Table table = repository.findById(tableId)
                .orElseThrow(()-> new  EntityNotFoundException(
                        "Table not found with id: " + tableId));

        repository.deleteById(tableId);
        log.info("table with "+ tableId + " is deleted.");
    }
}
