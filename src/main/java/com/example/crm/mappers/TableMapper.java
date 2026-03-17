package com.example.crm.mappers;

import com.example.crm.entity.Table;
import com.example.crm.entity.dtos.request.TableRequestDTO;
import com.example.crm.entity.dtos.response.TableResponseDTO;
import com.example.crm.util.enums.TableStatus;
import org.springframework.stereotype.Component;

@Component
public class TableMapper {

    public Table toEntity(TableRequestDTO dto) {
        Table table = new Table();
        table.setTableNumber(dto.getTableNumber());
        table.setCapacity(dto.getCapacity());
        table.setStatus(dto.getStatus() != null ? dto.getStatus() : TableStatus.AVAILABLE);
        return table;
    }

    public void updateEntity(Table table, TableRequestDTO dto) {
        table.setTableNumber(dto.getTableNumber());
        table.setCapacity(dto.getCapacity());
        if (dto.getStatus() != null) {
            table.setStatus(dto.getStatus());
        }
    }

    public TableResponseDTO toResponse(Table table) {
        return TableResponseDTO.builder()
                .tableId(table.getTableId())
                .tableNumber(table.getTableNumber())
                .capacity(table.getCapacity())
                .status(table.getStatus())
                .build();
    }
}
