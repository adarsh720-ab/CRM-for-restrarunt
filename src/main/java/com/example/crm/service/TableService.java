package com.example.crm.service;

import com.example.crm.entity.Table;
import com.example.crm.entity.dtos.request.TableRequestDTO;
import com.example.crm.entity.dtos.request.TableStatusUpdateRequestDTO;
import com.example.crm.entity.dtos.response.TableResponseDTO;
import com.example.crm.util.enums.TableStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface TableService {

    TableResponseDTO createTable(TableRequestDTO requestDTO);

    List<TableResponseDTO> getAllTables();

    TableResponseDTO getTableByTableNumber(Integer tableNumber);

    List<TableResponseDTO> getTableByStatus(TableStatus status);

    TableResponseDTO getTableById(UUID tableId);

    TableResponseDTO updateTable(UUID tableId, TableRequestDTO requestDTO);

    TableResponseDTO updateTableStatus(UUID tableId, TableStatus status);

    void deleteTable(UUID tableId);



}
